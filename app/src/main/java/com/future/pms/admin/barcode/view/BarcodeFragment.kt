package com.future.pms.admin.barcode.view

import android.content.Context.MODE_PRIVATE
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.barcode.injection.BarcodeComponent
import com.future.pms.admin.barcode.injection.DaggerBarcodeComponent
import com.future.pms.admin.barcode.presenter.BarcodePresenter
import com.future.pms.admin.core.base.BaseFragment
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import com.future.pms.admin.databinding.FragmentBarcodeBinding
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.BARCODE_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_barcode.*
import timber.log.Timber
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class BarcodeFragment : BaseFragment(), BarcodeContract {
  private var daggerBuild: BarcodeComponent = DaggerBarcodeComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: BarcodePresenter
  private lateinit var binding: FragmentBarcodeBinding
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = BARCODE_FRAGMENT
  }

  fun newInstance(): BarcodeFragment {
    return BarcodeFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode, container, false)
    with(binding) {
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTICATION, MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.attach(this)
    presenter.apply {
      subscribe()
      loadData(accessToken)
    }
    binding.btnGenerateQr.setOnClickListener {
      binding.btnGenerateQr.isEnabled = false
      binding.btnGenerateQr.text = ""
      presenter.getQrImage(accessToken)
    }
    getDateNow()
  }

  override fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse) {
    with(binding) {
      tvParkingZoneName.text = getString(R.string.welcome_to_s, parkingZone.name)
      tvAddressPhone.text = getString(R.string.two_value_comma, parkingZone.address,
          parkingZone.phoneNumber)
      tvOpenHourPrice.text = getString(R.string.two_value_newline, parkingZone.openHour,
          Utils.thousandSeparator(parkingZone.price.toInt()))
    }
  }

  override fun getDateNow() {
    val currentDateTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(Date())
    val dateText = binding.dateNow
    dateText.text = String.format(getString(R.string.date_now), currentDateTimeString)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      binding.currentTime.visibility = View.VISIBLE
      binding.currentTime.base = SystemClock.elapsedRealtime() - ((LocalDateTime.now().atZone(
          ZoneId.systemDefault()).toInstant().toEpochMilli()) - (LocalDate.now().atStartOfDay(
          ZoneId.systemDefault()).toInstant().toEpochMilli()))
      binding.currentTime.start()
    }
  }

  override fun getQrImageSuccess(imageName: String) {
    with(binding) {
      btnGenerateQr.setBackgroundResource(R.drawable.card_layout_grey_button_generate)
      btnGenerateQr.isClickable = false
      tvScan.visibility = View.VISIBLE
      tvMe.visibility = View.VISIBLE
    }
    Glide.with(binding.root).load(imageName).transform(CenterCrop()).placeholder(
        R.drawable.generate_qr).error(R.drawable.generate_qr).fallback(R.drawable.generate_qr).into(
        binding.ivQrcode)
    val cT = object : CountDownTimer(20000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        val va = ((millisUntilFinished % 60000) / 1000).toInt()
        binding.btnGenerateQr.text = String.format(getString(R.string.expires_in),
            String.format("%02d", millisUntilFinished / 60000), String.format("%02d", va))
      }

      override fun onFinish() {
        binding.btnGenerateQr.text = getString(R.string.generate_qr)
        binding.btnGenerateQr.isEnabled = true
      }
    }
    cT.start()
    Handler().postDelayed({
      with(binding) {
        btnGenerateQr.refreshDrawableState()
        btnGenerateQr.setBackgroundResource(R.drawable.card_layout_purple_button_generate)
        btnGenerateQr.isClickable = true
        tvScan.visibility = View.GONE
        tvMe.visibility = View.GONE
        ivQrcode.setImageResource(R.drawable.generate_qr)
      }
    }, 20000)
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar) {
      if (show) {
        progressBar.visibility = View.VISIBLE
      } else {
        progressBar.visibility = View.GONE
      }
    }
  }

  override fun showProgressTop(show: Boolean) {
    if (null != progressBarTop) {
      if (show) {
        progressBarTop.visibility = View.VISIBLE
      } else {
        progressBarTop.visibility = View.GONE
      }
    }
  }

  override fun onFailed(message: String) {
    if (message.contains(Constants.NO_CONNECTION)) {
      Toast.makeText(context, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      binding.ibRefresh.visibility = View.VISIBLE
      binding.ibRefresh.setOnClickListener {
        presenter.loadData(accessToken)
        binding.ibRefresh.visibility = View.GONE
      }
    } else {
      binding.btnGenerateQr.isEnabled = true
      binding.btnGenerateQr.text = getString(R.string.generate_qr)
      activity?.let {
        Snackbar.make(it.findViewById(android.R.id.content), getString(R.string.parking_slot_full),
            Snackbar.LENGTH_SHORT).show()
      }
      Timber.tag(Constants.ERROR).e(message)
    }
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}