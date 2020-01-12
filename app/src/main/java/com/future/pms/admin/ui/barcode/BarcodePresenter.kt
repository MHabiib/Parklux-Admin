package com.future.pms.admin.ui.barcode

import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BarcodePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: BarcodeContract

  fun loadData(accessToken: String) {
    view.showProgressTop(true)
    val subscribe = api.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZone ->
      view.showProgressTop(false)
      view.loadCustomerDetailSuccess(parkingZone.parkingZoneResponse)
    }, { error ->
      view.showProgressTop(false)
      view.showError(error.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getQrImage(accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getQrImage(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.showProgress(false)
      view.getQrImageSuccess(it.string())
    }, { error ->
      view.showProgress(false)
      view.showErrorMessage(error.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: BarcodeContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}