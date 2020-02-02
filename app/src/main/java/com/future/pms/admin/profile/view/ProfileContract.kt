package com.future.pms.admin.profile.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.response.ParkingZoneResponse

interface ProfileContract : BaseView {
  fun onSuccess()
  fun showProgress(show: Boolean)
  fun loadParkingZoneDetailSuccess(parkingZone: ParkingZoneResponse)
  fun onLogout()
}