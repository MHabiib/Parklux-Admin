package com.future.pms.admin.ui.profile

import com.future.pms.admin.model.response.ParkingZoneResponse

interface ProfileContract {
  fun onSuccess()
  fun onFailed(e: String)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse)
  fun onLogout()
}