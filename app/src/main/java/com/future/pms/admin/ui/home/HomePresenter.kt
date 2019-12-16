package com.future.pms.admin.ui.home

import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: HomeContract

  fun getParkingLayout(idLevel: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getParkingLayout(idLevel, accessToken).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.getLayoutSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.getLayoutFailed(it1) }
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun getLevels(accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getLevels(accessToken).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.getLevelsSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.showErrorMessage(it1) }
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun getSectionDetails(idLevel: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getSectionDetails(idLevel, accessToken).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.getSectionDetailsSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.showErrorMessage(it1) }
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun updateParkingSection(idSection: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.updateParkingSection(idSection, accessToken).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.updateParkingSectionSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.showErrorMessage(it1) }
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun updateLevel(idLevel: String, slotsLayout: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.updateLevel(idLevel, slotsLayout, accessToken).subscribeOn(
        Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.updateParkingLayoutSuccess(it)
      }
    }, {
      it.message?.let { itl -> view.showErrorMessage(itl) }
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun attach(view: HomeContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}