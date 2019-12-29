package com.future.pms.admin.ui.main

interface MainContract {
  fun showHomeFragment()
  fun showBarcodeFragment()
  fun showActivityListFragment()
  fun showProfileFragment()
  fun showEditLevel(idLevel: String, levelName: String, levelStatus: String)
  fun onBackPressedUpdateLevel()
}