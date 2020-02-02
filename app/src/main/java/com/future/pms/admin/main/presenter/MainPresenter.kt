package com.future.pms.admin.main.presenter

import com.future.pms.admin.main.view.MainContract
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

  fun showEditLevel(idLevel: String, nameLevel: String, levelStatus: String, totalTakenSlot: Int) {
    view.showEditLevel(idLevel, nameLevel, levelStatus, totalTakenSlot)
  }

  fun onBackPressedUpdateLevel() {
    view.onBackPressedUpdateLevel()
  }
}