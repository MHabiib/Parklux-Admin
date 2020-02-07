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
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.ADMIN_MODE
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.BARCODE_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.COUNT_DOWN_INTERVAL
import com.future.pms.admin.util.Constants.Companion.MILLIS_IN_A_MINUTES
import com.future.pms.admin.util.Constants.Companion.MILLIS_TO_SECOND
import com.future.pms.admin.util.Constants.Companion.QR_EXPIRED
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
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
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentBarcodeBinding
  private lateinit var accessToken: String
  private var count = 0
  private var startMillis: Long = 0
  private var mode: String = ""
  private lateinit var fcmToken: String
  private var countDownTimer: CountDownTimer? = null

  companion object {
    const val TAG: String = BARCODE_FRAGMENT
  }

  fun newInstance(): BarcodeFragment = BarcodeFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
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
      presenter.getQrImage(accessToken, fcmToken)
    }
    getDateNow()

    binding.ivQrcode.setOnClickListener {
      val time = System.currentTimeMillis()
      if (startMillis == 0L || (time - startMillis > 3000)) {
        startMillis = time
        count = 1
      } else {
        count++
      }
      if (count == 5) {
        val activity = activity as MainActivity?
        activity?.presenter?.onScanIconClick()
      }
    }

    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
      if (task.isSuccessful) {
        fcmToken = task.result?.token.toString() //asyc
      }
    }
  }

  override fun onResume() {
    super.onResume()
    if (mode == ADMIN_MODE) {
      val navigationView = activity?.findViewById(R.id.nav_view) as BottomNavigationView
      navigationView.visibility = View.GONE
    }
  }

  override fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse) {
    if (activity != null) {
      with(binding) {
        tvParkingZoneName.text = getString(R.string.welcome_to_s, parkingZone.name)
        tvAddressPhone.text = getString(R.string.two_value_comma, parkingZone.address,
            parkingZone.phoneNumber)
        tvOpenHourPrice.text = getString(R.string.two_value_newline, parkingZone.openHour,
            Utils.thousandSeparator(parkingZone.price.toInt()))
      }
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
    countDownTimer = object : CountDownTimer(QR_EXPIRED, COUNT_DOWN_INTERVAL) {
      override fun onTick(millisUntilFinished: Long) {
        val countdown = ((millisUntilFinished % MILLIS_IN_A_MINUTES) / MILLIS_TO_SECOND).toInt()
        binding.btnGenerateQr.text = String.format(getString(R.string.expires_in),
            String.format("%02d", millisUntilFinished / MILLIS_IN_A_MINUTES),
            String.format("%02d", countdown))
      }

      override fun onFinish() {
        binding.btnGenerateQr.text = getString(R.string.generate_qr)
        binding.btnGenerateQr.isEnabled = true
      }
    }
    (countDownTimer as CountDownTimer).start()
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
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  override fun showProgressTop(show: Boolean) {
    if (show) {
      binding.progressBarTop.visibility = View.VISIBLE
    } else {
      binding.progressBarTop.visibility = View.GONE
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
      if (activity != null) {
        binding.btnGenerateQr.isEnabled = true
        binding.btnGenerateQr.text = getString(R.string.generate_qr)
        activity?.let {
          Snackbar.make(it.findViewById(android.R.id.content),
              getString(R.string.parking_slot_full), Snackbar.LENGTH_SHORT).show()
        }
        Timber.tag(Constants.ERROR).e(message)
      }
    }
  }

  override fun onDestroy() {
    presenter.detach()
    countDownTimer?.cancel()
    super.onDestroy()
  }

  override fun onPause() {
    binding.btnGenerateQr.text = getString(R.string.generate_qr)
    binding.btnGenerateQr.isEnabled = true
    super.onPause()
  }
}