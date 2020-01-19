package com.future.pms.admin.ui.profile

import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.ui.base.BaseView

interface ProfileContract : BaseView {
  fun onSuccess()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse)
  fun onLogout()
}