package com.future.pms.admin.ui.barcode

import com.future.pms.admin.model.profile.ParkingZone

interface BarcodeContract {
  fun getDateNow()
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun unauthorized()
  fun loadCustomerDetailSuccess(parkingZone: ParkingZone)
    fun getQrImageSuccess(imageName: String)
}