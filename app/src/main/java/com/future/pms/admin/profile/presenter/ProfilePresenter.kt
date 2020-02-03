package com.future.pms.admin.profile.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import com.future.pms.admin.profile.network.ProfileApi
import com.future.pms.admin.profile.view.ProfileContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfilePresenter @Inject constructor(private val profileApi: ProfileApi) :
    BasePresenter<ProfileContract>() {

  fun loadData(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          profileApi.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ parkingZone: ParkingZoneResponse ->
            showProgress(false)
            loadParkingZoneDetailSuccess(parkingZone)
          }, { error ->
            showProgress(false)
            onFailed(error.toString())
          }))
    }
  }

  fun update(token: String, parkingZone: ParkingZoneResponse) {
    view?.apply {
      showProgress(true)
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
}