package com.future.pms.admin.ui.profile

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentProfileBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.ui.login.LoginActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.PROFILE_FRAGMENT
import com.future.pms.admin.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
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
      openHour.setOnClickListener { context?.let { it1 -> getDate(openHour, it1) } }
      openHour2.setOnClickListener { context?.let { it1 -> getDate(openHour2, it1) } }
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

  override fun unauthorized() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
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
      password.hint = "********"
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