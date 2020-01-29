package com.future.pms.admin.profile.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod.SHOW_FORCED
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.barcode.view.BarcodeFragment
import com.future.pms.admin.core.base.BaseFragment
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.databinding.FragmentProfileBinding
import com.future.pms.admin.login.view.LoginActivity
import com.future.pms.admin.profile.injection.DaggerProfileComponent
import com.future.pms.admin.profile.injection.ProfileComponent
import com.future.pms.admin.profile.presenter.ProfilePresenter
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.BAD_REQUEST_CODE
import com.future.pms.admin.util.Constants.Companion.NOT_FOUND_CODE
import com.future.pms.admin.util.Constants.Companion.NO_CONNECTION
import com.future.pms.admin.util.Constants.Companion.PROFILE_FRAGMENT
import com.future.pms.admin.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileContract {
  private var daggerBuild: ProfileComponent = DaggerProfileComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var binding: FragmentProfileBinding
  private lateinit var accessToken: String

  companion object {
    const val PERMISSION_REQUEST_CODE = 707
    const val TAG: String = PROFILE_FRAGMENT
  }

  fun newInstance(): ProfileFragment {
    return ProfileFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    with(binding) {

      btnLogout.setOnClickListener {
        btnLogout.visibility = View.GONE
        context?.let { it1 -> Authentication.delete(it1) }
        onLogout()
      }

      var profileNameTxt = ""
      var profileEmailTxt = ""
      var profilePhoneNumberTxt = ""
      var priceTxt = ""
      var openHourTxt = ""
      var openHour2Txt = ""
      var addressTxt = ""
      val passwordTxt = ""

      btnEditProfile.setOnClickListener {
        if (btnSaveProfile.visibility != View.VISIBLE) {
          btnSaveProfile.visibility = View.VISIBLE
          btnEditProfile.text = getString(R.string.cancel)
          profileNameTxt = profileName.text.toString()
          profileEmailTxt = profileEmail.text.toString()
          profilePhoneNumberTxt = profilePhoneNumber.text.toString()
          priceTxt = price.text.toString()
          openHourTxt = openHour.text.toString()
          openHour2Txt = openHour2.text.toString()
          addressTxt = address.text.toString()
          profileName.isEnabled = true
          profileName.isCursorVisible = true
          profileName.requestFocus()
          showKeyboard()
          profileEmail.isEnabled = true
          profilePhoneNumber.isEnabled = true
          price.isEnabled = true
          openHour.isEnabled = true
          openHour2.isEnabled = true
          address.isEnabled = true
          password.isEnabled = true
        } else {
          profileName.setText(profileNameTxt)
          profileEmail.setText(profileEmailTxt)
          profilePhoneNumber.setText(profilePhoneNumberTxt)
          price.setText(priceTxt)
          openHour.text = openHourTxt
          openHour2.text = openHour2Txt
          address.setText(addressTxt)
          password.setText(passwordTxt)
          exitEditMode()
        }
      }

      btnSaveProfile.setOnClickListener {
        if (isValid()) {
          exitEditMode()
          val price = binding.price.text.toString()
          val priceInDouble: Double = if (price == "") {
            0.0
          } else {
            price.toDouble()
          }
          val parkingZone = ParkingZoneResponse(
            binding.address.text.toString(),
            binding.profileEmail.text.toString(), binding.profileName.text.toString(),
            String.format(getString(R.string.range2), binding.openHour.text.toString(),
              binding.openHour2.text.toString()
            ), binding.password.text.toString(),
            binding.profilePhoneNumber.text.toString(), priceInDouble, "")
          presenter.update(accessToken, parkingZone)
        } else {
          Toast.makeText(
            context, "Please fill all the entries with valid input",
            Toast.LENGTH_LONG
          ).show()
        }
      }

      openHour.setOnClickListener { context?.let { context -> getDate(openHour, context) } }
      openHour2.setOnClickListener { context?.let { context -> getDate(openHour2, context) } }

      ivParkingZoneImage.setOnClickListener {
        if (checkPermission()) {
          getImageFromGallery()
        } else {
          requestPermission()
        }
      }
      return root
    }
  }

  private fun showKeyboard() {
    val view = activity?.currentFocus
    view?.let {
      val mInputMethodManager = activity?.getSystemService(
        Activity.INPUT_METHOD_SERVICE
      ) as InputMethodManager
      mInputMethodManager.toggleSoftInput(SHOW_FORCED, 0)
    }
  }

  private fun FragmentProfileBinding.exitEditMode() {
    btnEditProfile.text = getString(R.string.edit_profile)
    btnSaveProfile.visibility = View.GONE
    btnEditProfile.setTextColor(resources.getColor(R.color.colorAccent))
    profileName.isEnabled = false
    profileEmail.isEnabled = false
    profilePhoneNumber.isEnabled = false
    price.isEnabled = false
    openHour.isEnabled = false
    openHour2.isEnabled = false
    address.isEnabled = false
    password.isEnabled = false
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      subscribe()
      loadData(accessToken)
    }
  }

  private fun getImageFromGallery() {
    val photoPickerIntent = Intent(Intent.ACTION_PICK)
    photoPickerIntent.type = "image/*"
    startActivityForResult(photoPickerIntent, 69)
  }

  override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(reqCode, resultCode, data)
    if (resultCode == RESULT_OK) {
      try {
        val selectedImage = BitmapFactory.decodeStream(
            data?.data?.let { activity?.contentResolver?.openInputStream(it) })
        presenter.addPicture(accessToken,
            MultipartBody.Part.createFormData(getString(R.string.file),
                getString(R.string.image_name),
                RequestBody.create(MediaType.parse(getString(R.string.image_jpeg)),
                    persistImage(selectedImage))))
        binding.ivParkingZoneImage.setImageBitmap(selectedImage)
      } catch (e: FileNotFoundException) {
        e.printStackTrace()
        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
      }
    } else {
      Toast.makeText(context, getString(R.string.you_havent_picked_image), Toast.LENGTH_LONG).show()
    }
  }

  private fun persistImage(bitmap: Bitmap): File {
    val filesDir = context?.filesDir
    val imageFile = File(filesDir, getString(R.string.image_name))
    val os: OutputStream
    return try {
      os = FileOutputStream(imageFile)
      bitmap.compress(Bitmap.CompressFormat.JPEG, 30, os)
      os.flush()
      os.close()
      imageFile
    } catch (e: Exception) {
      imageFile
    }
  }

  override fun onSuccess() {
    val barcodeFragment = fragmentManager?.findFragmentByTag(BarcodeFragment.TAG) as BarcodeFragment
    barcodeFragment.presenter.loadData(accessToken)
    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
    presenter.loadData(accessToken)
  }

  override fun onFailed(message: String) {
    when {
      message.contains(NO_CONNECTION) -> Toast.makeText(context,
          getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      message.contains(BAD_REQUEST_CODE) -> Toast.makeText(context,
          "Failed to update profile, email already used !", Toast.LENGTH_SHORT).show()
      message.contains(NOT_FOUND_CODE) -> {
        context?.let { Authentication.delete(it) }
        onLogout()
      }
    }
    binding.ibRefresh.visibility = View.VISIBLE
    binding.ibRefresh.setOnClickListener {
      presenter.loadData(accessToken)
      binding.ibRefresh.visibility = View.GONE
    }
    Timber.tag(Constants.ERROR).e(message)
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

  override fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse) {
    with(binding) {
      profileNameDisplay.text = parkingZone.name
      profileName.setText(parkingZone.name)
      profileEmail.setText(parkingZone.emailAdmin)
      profilePhoneNumber.setText(parkingZone.phoneNumber)
      price.text = null
      price.hint = (String.format(getString(R.string.idr_price),
          Utils.thousandSeparator(parkingZone.price.toInt())))
      openHour.text = parkingZone.openHour.substring(0, 5)
      openHour2.text = parkingZone.openHour.substring(8, 13)
      address.setText(parkingZone.address)
      password.hint = getString(R.string.password_hint)

      Glide.with(binding.root).load(parkingZone.imageUrl).transform(CenterCrop(),
          RoundedCorners(80)).signature(ObjectKey(parkingZone.imageUrl)).placeholder(
          R.drawable.ic_parking_zone_default).error(
          R.drawable.ic_parking_zone_default).fallback(R.drawable.ic_parking_zone_default).into(
          binding.ivParkingZoneImage)
    }
  }

  private fun isValid(): Boolean {
    with(binding) {
      if (profileName?.text.toString().isEmpty()) return false
      if (!profileEmail?.text.toString().isEmailValid()) return false
      if (profilePhoneNumber?.text.toString().isEmpty()) return false
      if (openHour?.text.toString().isEmpty()) return false
      if (openHour2?.text.toString().isEmpty()) return false
      if (address?.text.toString().isEmpty()) return false
    }
    return true
  }

  private fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
  }

  @SuppressLint("SimpleDateFormat") private fun getDate(textView: TextView, context: Context) {
    val cal = Calendar.getInstance()
    val dateSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      cal.set(Calendar.HOUR_OF_DAY, hour)
      cal.set(Calendar.MINUTE, minute)
      textView.text = SimpleDateFormat("HH:mm").format(cal.time)
    }

    textView.setOnClickListener {
      TimePickerDialog(context, dateSetListener, cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), true).show()
    }
  }

  private fun checkPermission(): Boolean {
    val result = context?.let {
      ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    return result == PackageManager.PERMISSION_GRANTED
  }

  private fun requestPermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      requestAppPermissions()
    } else {
      activity?.let {
        ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE)
      }
    }
  }

  private fun requestAppPermissions() {
    activity?.let {
      ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }
  }

  override fun onLogout() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
    activity?.finish()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}