package com.future.pms.admin.ui.main

import javax.inject.Inject

class MainPresenter @Inject constructor() {
  private lateinit var view: MainContract

  fun attach(view: MainContract) {
    this.view = view
  }

  fun onHomeIconClick() {
    view.showHomeFragment()
  }

  fun onBarcodeIconClick() {
    view.showBarcodeFragment()
  }

    fun onActivityListIconClick() {
        view.showActivityListFragment()
    }

  fun onProfileIconClick() {
    view.showProfileFragment()
  }
}