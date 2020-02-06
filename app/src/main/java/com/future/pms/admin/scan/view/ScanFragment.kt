package com.future.pms.admin.scan.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.databinding.FragmentScanBinding
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.scan.injection.DaggerScanComponent
import com.future.pms.admin.scan.injection.ScanComponent
import com.future.pms.admin.scan.model.CustomerBooking
import com.future.pms.admin.scan.presenter.ScanPresenter
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.SCAN_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Utils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_scan.*
import java.io.IOException
import javax.inject.Inject

class ScanFragment : Fragment(), ScanContract {
  private var daggerBuild: ScanComponent = DaggerScanComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ScanPresenter
  @Inject lateinit var gson: Gson
  private var barcodeDetector: BarcodeDetector? = null
  private var cameraSource: CameraSource? = null
  private lateinit var intentData: String
  private lateinit var accessToken: String
  private lateinit var binding: FragmentScanBinding

  companion object {
    const val TAG: String = SCAN_FRAGMENT
  }

  fun newInstance(): ScanFragment = ScanFragment()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false)
    binding.btnScanQr.setOnClickListener {
      refreshPage()
    }
    binding.btnDisplayModeQr.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.onBarcodeIconClick()
      binding.cameraLayout.visibility = View.VISIBLE
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            TOKEN, null), Token::class.java).accessToken
    initialiseDetectorsAndSources()
  }

  override fun showProgress(show: Boolean) {
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun initialiseDetectorsAndSources() {
    barcodeDetector = BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build()

    cameraSource = CameraSource.Builder(context, barcodeDetector).setRequestedPreviewSize(1920,
        1080).setAutoFocusEnabled(true).build()

    surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        try {
          cameraSource?.start(surfaceView.holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        try {
          cameraSource?.start(surfaceView.holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource?.stop()
      }
    })

    barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {
        //No implementation required
      }

      override fun receiveDetections(detections: Detector.Detections<Barcode>) {
        val barcode = detections.detectedItems
        if (barcode.size() != 0 && barcode.valueAt(0).displayValue.startsWith("QR")) {
          binding.surfaceView.post {
            cameraSource?.stop()
            binding.cameraLayout.visibility = View.GONE
            showProgress(true)
            intentData = barcode.valueAt(0).displayValue
            val idSlot = intentData.substringAfter("idSlot=").substringBefore(')')
            val fcm = intentData.substringAfter(")")
            presenter.checkoutBookingStepTwo(idSlot, fcm, accessToken)
            binding.btnScanQr.isEnabled = true
            binding.btnScanQr.setBackgroundResource(R.drawable.card_layout_purple_button_generate)
          }
        }
      }
    })
  }

  override fun bookingSuccess(customerBooking: CustomerBooking) {
    showProgress(false)
    context?.let {
      val totalPrice = Utils.thousandSeparator(customerBooking.totalPrice.toString().substring(0,
          customerBooking.totalPrice.toString().length - 2).toInt())
      Utils.simpleDialogMessage(it, getString(R.string.checkout_booking_success),
          "Total price is IDR $totalPrice")
    }
  }

  override fun onFailed(message: String) {
    showProgress(false)
    context?.let {
      Utils.simpleDialogMessage(it, getString(R.string.checkout_booking_failed),
          "Something went wrong")
    }
  }

  private fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}