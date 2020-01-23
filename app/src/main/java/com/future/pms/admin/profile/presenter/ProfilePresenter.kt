package com.future.pms.admin.profile.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.profile.network.ProfileApi
import com.future.pms.admin.profile.view.ProfileContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileContract>() {
  @Inject lateinit var profileApi: ProfileApi

  fun loadData(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          profileApi.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZoneResponse ->
            showProgress(false)
            loadCustomerDetailSuccess(parkingZone)
          }, { error ->
            showProgress(false)
            onFailed(error.toString())
          }))
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
      subscriptions.add(
          profileApi.updateParkingZone(token, parkingZone).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            showProgress(false)
            onSuccess()
          }, {
            showProgress(false)
            onFailed(it.message.toString())
          }))
    }
  }

  fun addPicture(accessToken: String, picture: MultipartBody.Part) {
    view?.apply {
      showProgress(true)
      val subscribe = profileApi.updateParkingZonePicture(accessToken, picture).subscribeOn(
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