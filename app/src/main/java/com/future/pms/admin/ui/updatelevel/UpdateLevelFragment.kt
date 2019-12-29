package com.future.pms.admin.ui.updatelevel

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
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentUpdateLevelBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.ui.home.HomeFragment
import com.future.pms.admin.ui.main.MainActivity
import com.future.pms.admin.util.Constants.Companion.AUTHENTCATION
import com.future.pms.admin.util.Constants.Companion.DELETE_LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.ID_LEVEL
import com.future.pms.admin.util.Constants.Companion.LEVEL_AVAILABLE
import com.future.pms.admin.util.Constants.Companion.LEVEL_NAME
import com.future.pms.admin.util.Constants.Companion.LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Constants.Companion.UPDATE_LEVEL_FRAGMENT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class UpdateLevelFragment : Fragment(), UpdateLevelContract {
  @Inject lateinit var presenter: UpdateLevelPresenter
  private lateinit var binding: FragmentUpdateLevelBinding
  private lateinit var idLevel: String
  private lateinit var levelName: String
  private lateinit var levelStatus: String

  companion object {
    const val TAG: String = UPDATE_LEVEL_FRAGMENT
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
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
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTCATION, MODE_PRIVATE)?.getString(TOKEN, null),
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
        presenter.updateParkingLevel(accessToken, idLevel, txtLevelName.text.toString(),
            rgStatus.checkedRadioButtonId)
      }
      btnDelete.setOnClickListener {
        presenter.updateParkingLevel(accessToken, idLevel, txtLevelName.text.toString(),
            DELETE_LEVEL_STATUS)
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
    }
  }

  override fun showErrorMessage() {
    MaterialAlertDialogBuilder(context).setTitle(getString(R.string.failed)).setMessage(
            getString(R.string.still_ongoing_parking)).setPositiveButton(getString(R.string.ok),
            null).show()
  }

  override fun updateParkingLevelSuccess(response: Response<Void>) {
    val activity = activity as MainActivity?
    activity?.presenter?.onBackPressedUpdateLevel()
    val fm = fragmentManager
    val homeFragment = fm?.findFragmentByTag(HOME_FRAGMENT) as HomeFragment
    homeFragment.refreshPage()
    Toast.makeText(context, getString(R.string.success_high), Toast.LENGTH_SHORT).show()
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}