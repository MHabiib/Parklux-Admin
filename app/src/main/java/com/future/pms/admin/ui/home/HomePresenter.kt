package com.future.pms.admin.ui.home

import com.future.pms.admin.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor() : BasePresenter<HomeContract>() {

  fun getParkingLayout(idLevel: String, accessToken: String) {
    val subscribe = api.getParkingLayout(idLevel, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.getLayoutSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun getLevels(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getLevels(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          showProgress(false)
          getLevelsSuccess(it)
        }
      }, {
        showProgress(false)
        it.message?.let { throwable -> onFailed(throwable) }
      })
      subscriptions.add(subscribe)
    }
  }

  fun addParkingLevel(levelName: String, accessToken: String) {
    val subscribe = api.addParkingLevel(levelName, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.addParkingLevelSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun getSectionDetails(idLevel: String, accessToken: String) {
    val subscribe = api.getSectionDetails(idLevel, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.getSectionDetailsSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun updateParkingSection(idSection: String, accessToken: String) {
    val subscribe = api.updateParkingSection(idSection, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.updateParkingSectionSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun updateLevel(idLevel: String, slotsLayout: String, accessToken: String) {
    val subscribe = api.updateLevel(idLevel, slotsLayout, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.updateParkingLayoutSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun editModeParkingLevel(idLevel: String, mode: String, accessToken: String) {
    val subscribe = api.editModeParkingLevel(idLevel, mode, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.editModeParkingLevelSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: HomeContract) {
    this.view = view
  }
}