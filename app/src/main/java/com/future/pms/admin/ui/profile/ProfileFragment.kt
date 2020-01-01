package com.future.pms.admin.ui.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentProfileBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.network.NetworkConstant
import com.future.pms.admin.ui.login.LoginActivity
import com.future.pms.admin.util.Constants
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

class ProfileFragment : Fragment(), ProfileContract {
  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var binding: FragmentProfileBinding
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = PROFILE_FRAGMENT
  }

  fun newInstance(): ProfileFragment {
    return ProfileFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    with(binding) {
      btnLogout.setOnClickListener {
        btnLogout.visibility = View.GONE
        presenter.signOut()
        onLogout()
      }
      btnSave.setOnClickListener {
        showProgress(true)
        presenter.update(binding.profileName.text.toString(), binding.profileEmail.text.toString(),
            binding.profilePhoneNumber.text.toString(), binding.price.text.toString(),
            String.format(getString(R.string.range2), binding.openHour.text.toString(),
                binding.openHour2.text.toString()), binding.address.text.toString(),
            binding.password.text.toString(), accessToken)
      }
      openHour.setOnClickListener { context?.let { context -> getDate(openHour, context) } }
      openHour2.setOnClickListener { context?.let { context -> getDate(openHour2, context) } }
      ivParkingZoneImage.setOnClickListener {
        getImageFromGallery()
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
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
        var selectedImage = BitmapFactory.decodeStream(
            data?.data?.let { activity?.contentResolver?.openInputStream(it) })
        selectedImage = rotateImage(selectedImage, 90F)
        val reqFile = RequestBody.create(MediaType.parse(getString(R.string.image_jpeg)),
            persistImage(selectedImage))
        val body = MultipartBody.Part.createFormData(getString(R.string.file),
            getString(R.string.image_name), reqFile)
        presenter.addPicture(accessToken, body)
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
      bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
      os.flush()
      os.close()
      imageFile
    } catch (e: Exception) {
      imageFile
    }
  }

  fun rotateImage(src: Bitmap, degree: Float): Bitmap {
    // create new matrix
    val matrix = Matrix()
    // setup rotation degree
    matrix.postRotate(degree)
    return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
  }

  override fun onSuccess() {
    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
    refreshPage()
  }

  override fun onFailed(e: String) {
    Timber.e(e)
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
    Timber.tag(Constants.ERROR).e(error)
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
      openHour2.text = parkingZone.openHour.substring(7, 13)
      address.setText(parkingZone.address)
      password.hint = getString(R.string.password_hint)
      Glide.with(binding.root).load(
          getString(R.string.picture_url, NetworkConstant.BASE, parkingZone.imageUrl)).transform(
          CenterCrop(), RoundedCorners(80)).diskCacheStrategy(
          DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(
              R.drawable.ic_parking_zone_default).error(
              R.drawable.ic_parking_zone_default).fallback(R.drawable.ic_parking_zone_default).into(
              binding.ivParkingZoneImage)
      profileNameDisplay.addTextChangedListener(textWatcher())
      profileName.addTextChangedListener(textWatcher())
      profileEmail.addTextChangedListener(textWatcher())
      profilePhoneNumber.addTextChangedListener(textWatcher())
      price.addTextChangedListener(textWatcher())
      openHour.addTextChangedListener(textWatcher())
      openHour2.addTextChangedListener(textWatcher())
      address.addTextChangedListener(textWatcher())
      password.addTextChangedListener(textWatcher())
    }
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

  private fun textWatcher(): TextWatcher {
    return object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        binding.btnSave.setBackgroundResource(R.drawable.card_layout_purple)
        binding.btnSave.isEnabled = true
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
  }

  private fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  override fun onLogout() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}