package com.future.pms.admin.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentProfileBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.ui.login.LoginActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.PROFILE_FRAGMENT
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract {
  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var binding: FragmentProfileBinding

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

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    with(binding) {
      btnLogout.setOnClickListener {
        btnLogout.visibility = View.GONE
        presenter.signOut()
        onLogout()
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    presenter.attach(this)
    presenter.apply {
      subscribe()
      loadData(accessToken)
    }
  }

  override fun onSuccess() {
    TODO(
      "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
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

  override fun loadCustomerDetailSuccess(parkingZone: ParkingZone) {
    with(binding) {
      profileNameDisplay.text = parkingZone.body.name
      profileName.setText(parkingZone.body.name)
      profileEmail.setText(parkingZone.body.emailAdmin)
      profilePhoneNumber.setText(parkingZone.body.phoneNumber)
      price.setText(parkingZone.body.price.toString())
      openHour.setText(parkingZone.body.openHour)
      address.setText(parkingZone.body.address)
      password.hint = "********"
      profileNameDisplay.addTextChangedListener(textWatcher())
      profileName.addTextChangedListener(textWatcher())
      profileEmail.addTextChangedListener(textWatcher())
      profilePhoneNumber.addTextChangedListener(textWatcher())
      price.addTextChangedListener(textWatcher())
      openHour.addTextChangedListener(textWatcher())
      address.addTextChangedListener(textWatcher())
      password.addTextChangedListener(textWatcher())
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

  override fun onLogout() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
      FragmentModule()
    ).build()
    profileComponent.inject(this)
  }
}