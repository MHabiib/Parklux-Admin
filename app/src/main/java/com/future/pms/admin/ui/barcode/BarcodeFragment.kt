package com.future.pms.admin.ui.barcode

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentBarcodeBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.network.NetworkConstant.BASE
import com.future.pms.admin.ui.login.LoginActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.AUTHENTCATION
import com.future.pms.admin.util.Constants.Companion.BARCODE_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class BarcodeFragment : Fragment(), BarcodeContract {
  @Inject lateinit var presenter: BarcodePresenter
  private lateinit var binding: FragmentBarcodeBinding

  companion object {
    const val TAG: String = BARCODE_FRAGMENT
  }

  fun newInstance(): BarcodeFragment {
    return BarcodeFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
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
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTCATION, MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.attach(this)
    presenter.apply {
      subscribe()
      loadData(accessToken)
    }
    binding.btnGenerateQr.setOnClickListener {
      presenter.getQrImage(accessToken)
    }
    getDateNow()
  }

  override fun loadCustomerDetailSuccess(parkingZone: ParkingZone) {
    with(binding) {
      tvParkingZoneName.text = getString(R.string.welcome_to_s, parkingZone.body.name)
      tvAddressPhone.text = getString(R.string.two_value_comma, parkingZone.body.address,
          parkingZone.body.phoneNumber)
      tvOpenHourPrice.text = getString(R.string.two_value_newline, parkingZone.body.openHour,
          Utils.thousandSeparator(parkingZone.body.price.toInt()))
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
    Glide.with(binding.root).load(getString(R.string.image_url, BASE, imageName)).transform(
        CenterCrop(), RoundedCorners(80)).placeholder(R.drawable.generate_qr).error(
        R.drawable.generate_qr).fallback(R.drawable.generate_qr).into(binding.ivQrcode)
    val cT = object : CountDownTimer(20000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        val va = ((millisUntilFinished % 60000) / 1000).toInt()
        binding.btnGenerateQr.text = String.format(getString(R.string.expires_in),
            String.format("%02d", millisUntilFinished / 60000), String.format("%02d", va))
      }

      override fun onFinish() {
        binding.btnGenerateQr.text = getString(R.string.generate_qr)
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

  override fun showErrorMessage(error: String) {
    activity?.let {
      Snackbar.make(it.findViewById(android.R.id.content), getString(R.string.parking_slot_full),
          Snackbar.LENGTH_LONG).show()
    }
    Timber.tag(Constants.ERROR).e(error)
  }

  override fun unauthorized() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}