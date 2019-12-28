package com.future.pms.admin.ui.main

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.future.pms.admin.R
import com.future.pms.admin.databinding.ActivityMainBinding
import com.future.pms.admin.di.component.DaggerActivityComponent
import com.future.pms.admin.di.module.ActivityModule
import com.future.pms.admin.ui.activitylist.ActivityListFragment
import com.future.pms.admin.ui.barcode.BarcodeFragment
import com.future.pms.admin.ui.home.HomeFragment
import com.future.pms.admin.ui.profile.ProfileFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract {
  @Inject lateinit var presenter: MainPresenter
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
        R.id.navigation_activity_list -> {
          presenter.onActivityListIconClick()
        }
      }
      return@setOnNavigationItemSelectedListener true
    }
    presenter.onHomeIconClick()
  }

  override fun showHomeFragment() {
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().add(R.id.frame, HomeFragment().newInstance(),
          HomeFragment.TAG).commit()
    } else {
      supportFragmentManager.beginTransaction().show(
          supportFragmentManager.findFragmentByTag(HomeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG)!!).commit()
    }
  }

  override fun showBarcodeFragment() {
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().add(R.id.frame, BarcodeFragment().newInstance(),
          BarcodeFragment.TAG).commit()
    } else {
      supportFragmentManager.beginTransaction().show(
          supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(HomeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG)!!).commit()
    }
  }

  override fun showActivityListFragment() {
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().add(R.id.frame,
          ActivityListFragment().newInstance(), ActivityListFragment.TAG).commit()
    } else {
      supportFragmentManager.beginTransaction().show(
          supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(HomeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)!!).commit()
    }
  }

  override fun showProfileFragment() {
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().add(R.id.frame, ProfileFragment().newInstance(),
          ProfileFragment.TAG).commit()
    } else {
      supportFragmentManager.beginTransaction().show(
          supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(BarcodeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(HomeFragment.TAG)!!).commit()
    }
    if (supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG) != null) {
      supportFragmentManager.beginTransaction().hide(
          supportFragmentManager.findFragmentByTag(ActivityListFragment.TAG)!!).commit()
    }
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()

    activityComponent.inject(this)
  }
}
