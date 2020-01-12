package com.future.pms.admin.ui.profile

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import com.future.pms.admin.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfilePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ProfileContract

  fun loadData(accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZone ->
      view.showProgress(false)
      view.loadCustomerDetailSuccess(parkingZone.parkingZoneResponse)
    }, { error ->
      view.showProgress(false)
      view.showErrorMessage(error.localizedMessage)
    })
    subscriptions.add(subscribe)
  }

  fun update(name: String, email: String, phoneNumber: String, price: String, openHour: String,
      address: String, password: String, token: String) {
    view.showProgress(true)
    val priceInDouble: Double = if (price == "") {
      0.0
    } else {
      price.toDouble()
    }
    val parkingZone = ParkingZoneResponse(address, email, name, openHour, password, phoneNumber,
        priceInDouble, "")
    val subscribe = api.updateParkingZone(token, parkingZone).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view.showProgress(false)
      view.onSuccess()
    }, {
      view.showProgress(false)
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun addPicture(accessToken: String, picture: MultipartBody.Part) {
    view.showProgress(true)
    val subscribe = api.updateParkingZonePicture(accessToken, picture).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view.showProgress(false)
      view.onSuccess()
    }, {
      view.showProgress(false)
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  private fun getContext(): Context? {
    return when (view) {
      is Fragment -> (view as Fragment).context
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }

  fun attach(view: ProfileContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}