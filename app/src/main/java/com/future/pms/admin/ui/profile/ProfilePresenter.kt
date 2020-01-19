package com.future.pms.admin.ui.profile

import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.ui.base.BasePresenter
import com.future.pms.admin.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileContract>() {

  fun loadData(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZoneResponse ->
        showProgress(false)
        loadCustomerDetailSuccess(parkingZone)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun update(name: String, email: String, phoneNumber: String, price: String, openHour: String,
      address: String, password: String, token: String) {
    view?.apply {
      showProgress(true)
      val priceInDouble: Double = if (price == "") {
        0.0
      } else {
        price.toDouble()
      }
      val parkingZone = ParkingZoneResponse(address, email, name, openHour, password, phoneNumber,
          priceInDouble, "")
      val subscribe = api.updateParkingZone(token, parkingZone).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        onSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun addPicture(accessToken: String, picture: MultipartBody.Part) {
    view?.apply {
      showProgress(true)
      val subscribe = api.updateParkingZonePicture(accessToken, picture).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        onSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }

  fun attach(view: ProfileContract) {
    this.view = view
  }
}