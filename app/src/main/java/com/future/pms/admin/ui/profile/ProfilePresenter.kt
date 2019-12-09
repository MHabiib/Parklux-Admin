package com.future.pms.admin.ui.profile

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.admin.model.oauth.profile.ParkingZone
import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import com.future.pms.admin.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ProfileContract

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