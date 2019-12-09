package com.future.pms.admin.ui.profile

import com.future.pms.admin.model.oauth.profile.ParkingZone

interface ProfileContract {
  fun onSuccess()
  fun onFailed(e: String)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun unauthorized()
  fun loadCustomerDetailSuccess(parkingZone: ParkingZone)
  fun onLogout()
}