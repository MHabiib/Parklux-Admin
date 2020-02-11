package com.future.pms.admin.barcode.presenter

import com.future.pms.admin.barcode.network.BarcodeApi
import com.future.pms.admin.barcode.view.BarcodeContract
import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BarcodePresenter @Inject constructor(private val barcodeApi: BarcodeApi) :
    BasePresenter<BarcodeContract>() {

  fun loadData(accessToken: String) {
    view?.apply {
      showProgressTop(true)
      subscriptions.add(
          barcodeApi.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZoneResponse ->
            showProgressTop(false)
            loadCustomerDetailSuccess(parkingZone)
          }, { error ->
            showProgressTop(false)
            onFailed(error.message.toString())
          }))
    }
  }

  fun getQrImage(accessToken: String, fcmToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          barcodeApi.getQrImage(fcmToken, accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        getQrImageSuccess(it.string())
      }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      }))
    }

  }
}