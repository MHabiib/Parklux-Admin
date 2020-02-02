package com.future.pms.admin.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.activitylist.view.ActivityListFragment
import com.future.pms.admin.barcode.view.BarcodeFragment
import com.future.pms.admin.core.base.BaseActivity
import com.future.pms.admin.databinding.ActivityMainBinding
import com.future.pms.admin.home.view.HomeFragment
import com.future.pms.admin.main.injection.DaggerMainComponent
import com.future.pms.admin.main.injection.MainComponent
import com.future.pms.admin.main.presenter.MainPresenter
import com.future.pms.admin.profile.view.ProfileFragment
import com.future.pms.admin.updatelevel.view.UpdateLevelFragment
import com.future.pms.admin.util.Constants.Companion.FCM_CUSTOMER_NAME
import com.future.pms.admin.util.Constants.Companion.FCM_LEVEL_NAME
import com.future.pms.admin.util.Constants.Companion.FCM_PARKING_ZONE
import com.future.pms.admin.util.Constants.Companion.ID_LEVEL
import com.future.pms.admin.util.Constants.Companion.LEVEL_NAME
import com.future.pms.admin.util.Constants.Companion.LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.MY_FIREBASE_MESSAGING
import com.future.pms.admin.util.Constants.Companion.TOTAL_TAKEN_SLOT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract {
  private var daggerBuild: MainComponent = DaggerMainComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: MainPresenter
  private lateinit var binding: ActivityMainBinding
  private lateinit var mTTS: TextToSpeech

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    presenter.attach(this)
    val navView = binding.navView
    if (savedInstanceState == null) {
      presenter.onHomeIconClick()
    }
    navView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {
          presenter.onHomeIconClick()
        }
        R.id.navigation_scan -> {
          presenter.onBarcodeIconClick()
        }
        R.id.navigation_profile -> {
          presenter.onProfileIconClick()
        }
        R.id.navigation_activity_list -> {
          presenter.onActivityListIconClick()
        }
      }
      return@setOnNavigationItemSelectedListener true
    }
    mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
      if (status != TextToSpeech.ERROR) {
        mTTS.language = Locale.US
      }
    })
  }

  override fun showHomeFragment() {
    binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, HomeFragment().newInstance(),
          HomeFragment.TAG).commit()
    } else {
      if (supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG) == null) {
        supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
          supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).show(it).commit()
        }
      } else {
        supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
          supportFragmentManager.beginTransaction().show(it).commit()
        }
        supportFragmentManager.run {
          findFragmentByTag(UpdateLevelFragment.TAG)
        }?.let {
          supportFragmentManager.beginTransaction().show(it).commit()
        }
      }
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(ProfileFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(BarcodeFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ActivityListFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
  }

  override fun showBarcodeFragment() {
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, BarcodeFragment().newInstance(),
          BarcodeFragment.TAG).commit()
    } else {
      supportFragmentManager.run {
        findFragmentByTag(BarcodeFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(ProfileFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ActivityListFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(UpdateLevelFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
  }

  override fun showActivityListFragment() {
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, ActivityListFragment().newInstance(),
          ActivityListFragment.TAG).commit()
    } else {
      supportFragmentManager.run { findFragmentByTag(ActivityListFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(BarcodeFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(ProfileFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(UpdateLevelFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
  }

  override fun showProfileFragment() {
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, ProfileFragment().newInstance(),
          ProfileFragment.TAG).commit()
    } else {
      supportFragmentManager.run {
        findFragmentByTag(ProfileFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(BarcodeFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ActivityListFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(UpdateLevelFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
  }

  override fun showEditLevel(idLevel: String, levelName: String, levelStatus: String,
      totalTakenSlot: Int) {
    val fragment = UpdateLevelFragment()
    val bundle = Bundle()
    bundle.putString(ID_LEVEL, idLevel)
    bundle.putString(LEVEL_NAME, levelName)
    bundle.putString(LEVEL_STATUS, levelStatus)
    bundle.putInt(TOTAL_TAKEN_SLOT, totalTakenSlot)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, fragment, UpdateLevelFragment.TAG).commit()
    }
  }

  override fun onBackPressedUpdateLevel() {
    supportFragmentManager.findFragmentByTag(UpdateLevelFragment.TAG)?.let {
      supportFragmentManager.beginTransaction().remove(it).commit()
    }
  }

  private var doubleBackToExitPressedOnce = false

  override fun onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      finishAffinity()
      return
    }

    this.doubleBackToExitPressedOnce = true
    Toast.makeText(this, getString(R.string.click_again_exit), Toast.LENGTH_SHORT).show()

    Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
  }

  override fun onResume() {
    super.onResume()

    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
        IntentFilter(MY_FIREBASE_MESSAGING))
  }

  private val mMessageReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val customerName = intent.getStringExtra(FCM_CUSTOMER_NAME)
      val parkingZoneName = intent.getStringExtra(FCM_PARKING_ZONE)
      val levelName = intent.getStringExtra(FCM_LEVEL_NAME)

      val title = String.format(getString(R.string.hi_dialog), customerName)
      val message = String.format(getString(R.string.thanks_using_dialog), parkingZoneName,
          levelName)
      showDialog(title, message)
      mTTS.speak(title + message, TextToSpeech.QUEUE_FLUSH, null)
    }
  }

  private fun showDialog(title: String, body: String) {
    val dialog = MaterialAlertDialogBuilder(this).setTitle(title).setMessage(body).show()
    Handler().postDelayed({
      dialog?.dismiss()
    }, 7000)
  }

  override fun onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    super.onPause()
  }

  override fun onFailed(message: String) = Timber.e(message)
}
