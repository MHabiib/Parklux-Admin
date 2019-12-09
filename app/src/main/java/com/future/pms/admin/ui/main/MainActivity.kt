package com.future.pms.admin.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.future.pms.admin.R
import com.future.pms.admin.databinding.ActivityMainBinding
import com.future.pms.admin.di.component.DaggerActivityComponent
import com.future.pms.admin.di.module.ActivityModule
import com.future.pms.admin.ui.barcode.BarcodeFragment
import com.future.pms.admin.ui.home.HomeFragment
import com.future.pms.admin.ui.profile.ProfileFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract {
  @Inject lateinit var presenter: MainPresenter
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    injectDependency()
    presenter.attach(this)
    val navView = binding.navView
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
      }
      return@setOnNavigationItemSelectedListener true
    }
    presenter.onHomeIconClick()
  }

  override fun showHomeFragment() {
    binding.navView.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left
      ).replace(
        R.id.frame, HomeFragment().newInstance(), HomeFragment.TAG
      ).commit()
    }
  }

  override fun showBarcodeFragment() {
    binding.navView.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
        R.id.frame, BarcodeFragment().newInstance(), BarcodeFragment.TAG
      ).commit()
    }
  }

  override fun showProfileFragment() {
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right
      ).replace(
        R.id.frame, ProfileFragment().newInstance(), ProfileFragment.TAG
      ).commit()
    }
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
      ActivityModule(this)
    ).build()

    activityComponent.inject(this)
  }
}
