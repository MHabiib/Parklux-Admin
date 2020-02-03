package com.future.pms.admin.updatelevel.view

import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.base.BaseFragment
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.request.LevelDetailsRequest
import com.future.pms.admin.databinding.FragmentUpdateLevelBinding
import com.future.pms.admin.home.view.HomeFragment
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.updatelevel.injection.DaggerUpdateLevelComponent
import com.future.pms.admin.updatelevel.injection.UpdateLevelComponent
import com.future.pms.admin.updatelevel.presenter.UpdateLevelPresenter
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.ID_LEVEL
import com.future.pms.admin.util.Constants.Companion.LEVEL_AVAILABLE
import com.future.pms.admin.util.Constants.Companion.LEVEL_NAME
import com.future.pms.admin.util.Constants.Companion.LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.LEVEL_TAKE_OUT
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Constants.Companion.TOTAL_TAKEN_SLOT
import com.future.pms.admin.util.Constants.Companion.UPDATE_LEVEL_FRAGMENT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import javax.inject.Inject

class UpdateLevelFragment : BaseFragment(), UpdateLevelContract {
  private var daggerBuild: UpdateLevelComponent = DaggerUpdateLevelComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: UpdateLevelPresenter
    @Inject
    lateinit var gson: Gson
  private lateinit var binding: FragmentUpdateLevelBinding
  private lateinit var idLevel: String
  private lateinit var levelName: String
  private lateinit var levelStatus: String
  private var totalTakenSlot = 0

  companion object {
    const val TAG: String = UPDATE_LEVEL_FRAGMENT
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onBackPressedUpdateLevel()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_level, container, false)
    idLevel = this.arguments?.getString(ID_LEVEL).toString()
    levelName = this.arguments?.getString(LEVEL_NAME).toString()
    levelStatus = this.arguments?.getString(LEVEL_STATUS).toString()
    if (this.arguments?.getInt(TOTAL_TAKEN_SLOT) != null) {
      totalTakenSlot = this.arguments?.getInt(TOTAL_TAKEN_SLOT).toString().toInt()
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
      val accessToken = gson.fromJson(
        context?.getSharedPreferences(AUTHENTICATION, MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.attach(this)
    with(binding) {
      txtLevelName.hint = levelName
      if (levelStatus == LEVEL_AVAILABLE) {
        rgStatus.check(R.id.rb_available)
      } else {
        rgStatus.check(R.id.rb_unavailable)
      }
      btnUpdate.setOnClickListener {
        val statusStr: String = if (rgStatus.checkedRadioButtonId == R.id.rb_available) {
          LEVEL_AVAILABLE
        } else {
          Constants.LEVEL_UNAVAILABLE
        }

        presenter.updateParkingLevel(accessToken,
            LevelDetailsRequest(idLevel, txtLevelName.text.toString(), statusStr))
      }
      btnDelete.setOnClickListener {
        presenter.updateParkingLevel(accessToken,
            LevelDetailsRequest(idLevel, txtLevelName.text.toString(), LEVEL_TAKE_OUT))
      }
      ibBack.setOnClickListener {
        val activity = activity as MainActivity?
        activity?.presenter?.onBackPressedUpdateLevel()
      }

      val rbUnavailable: RadioButton = binding.rbUnavailable
      rbUnavailable.setOnClickListener {
        MaterialAlertDialogBuilder(context).setTitle(getString(R.string.caution)).setMessage(
            getString(R.string.unavailable_details)).setPositiveButton(getString(R.string.ok),
            null).setNegativeButton(getString(R.string.cancel)) { _: DialogInterface, _: Int ->
          rgStatus.check(R.id.rb_available)
        }.show()
      }

      if (totalTakenSlot > 0) {
        btnDelete.visibility = View.GONE
        tvDeleteDetails.text = getString(R.string.cant_delete_level_details)
      } else {
        btnDelete.visibility = View.VISIBLE
        tvDeleteDetails.text = getString(R.string.delete_details)
      }
    }
  }

  override fun onFailed(message: String) {
    MaterialAlertDialogBuilder(context).setTitle(getString(R.string.failed)).setMessage(
        getString(R.string.still_ongoing_parking)).setPositiveButton(getString(R.string.ok),
        null).show()
  }

  override fun updateParkingLevelSuccess(response: String) {
    val activity = activity as MainActivity?
    activity?.presenter?.onBackPressedUpdateLevel()
    val fm = fragmentManager
    val homeFragment = fm?.findFragmentByTag(HOME_FRAGMENT) as HomeFragment
    homeFragment.refreshPage()
    Toast.makeText(context, getString(R.string.success_high), Toast.LENGTH_SHORT).show()
  }

  override fun showProgress(show: Boolean) {
    with(binding) {
      if (show) {
        ibBack.isEnabled = false
        btnUpdate.isEnabled = false
        btnDelete.isEnabled = false
        btnUpdate.setTextColor(resources.getColor(R.color.darkGrey))
        btnDelete.setTextColor(resources.getColor(R.color.darkGrey))
        progressBar.visibility = View.VISIBLE
      } else {
        ibBack.isEnabled = true
        btnUpdate.isEnabled = true
        btnDelete.isEnabled = true
        btnUpdate.setTextColor(resources.getColor(R.color.colorAccent))
        btnDelete.setTextColor(resources.getColor(R.color.red))
        progressBar.visibility = View.GONE
      }
    }
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}