package com.future.pms.admin.ui.barcode

import com.future.pms.admin.model.response.ParkingZoneResponse

interface BarcodeContract {
  fun getDateNow()
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun showError(error: String)
  fun loadCustomerDetailSuccess(parkingZone: ParkingZoneResponse)
  fun getQrImageSuccess(imageName: String)
  fun showProgressTop(show: Boolean)
}