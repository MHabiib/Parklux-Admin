package com.future.pms.admin.ui.barcode

import com.future.pms.admin.model.oauth.profile.ParkingZone
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
    val subscribe = api.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
      AndroidSchedulers.mainThread()
    ).subscribe({ parkingZone: ParkingZone ->
      view.loadCustomerDetailSuccess(parkingZone)
    }, { error ->
      view.showErrorMessage(error.localizedMessage)
      view.unauthorized()
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