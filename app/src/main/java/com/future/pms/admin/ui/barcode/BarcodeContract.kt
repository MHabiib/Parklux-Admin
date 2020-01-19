package com.future.pms.admin.ui.barcode

import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.ui.base.BaseView

interface BarcodeContract : BaseView {
  fun getDateNow()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse)
  fun getQrImageSuccess(imageName: String)
  fun showProgressTop(show: Boolean)
}