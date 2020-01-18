package com.future.pms.admin.ui.barcode

import com.future.pms.admin.di.base.BasePresenter
import com.future.pms.admin.model.response.ParkingZoneResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BarcodePresenter @Inject constructor() : BasePresenter<BarcodeContract>() {

  fun loadData(accessToken: String) {
    view?.apply {
      showProgressTop(true)
      val subscribe = api.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZoneResponse ->
        showProgressTop(false)
        loadCustomerDetailSuccess(parkingZone)
      }, { error ->
        showProgressTop(false)
        onFailed(error.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun getQrImage(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getQrImage(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        getQrImageSuccess(it.string())
      }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      })
      subscriptions.add(subscribe)
    }

  }

  fun attach(view: BarcodeContract) {
    this.view = view
  }
}