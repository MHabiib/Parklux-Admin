package com.future.pms.admin.barcode.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.response.ParkingZoneResponse

interface BarcodeContract : BaseView {
  fun getDateNow()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse)
  fun getQrImageSuccess(imageName: String)
  fun showProgressTop(show: Boolean)
}