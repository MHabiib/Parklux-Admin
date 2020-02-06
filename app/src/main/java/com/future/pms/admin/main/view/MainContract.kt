package com.future.pms.admin.main.view

interface MainContract {
  fun showHomeFragment()
  fun showBarcodeFragment()
  fun showScanFragment()
  fun showActivityListFragment()
  fun showProfileFragment()
  fun showEditLevel(idLevel: String, levelName: String, levelStatus: String, totalTakenSlot: Int)
  fun onBackPressedUpdateLevel()
}