package com.future.pms.admin.home.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.home.network.HomeApi
import com.future.pms.admin.home.view.HomeContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor() : BasePresenter<HomeContract>() {
  @Inject lateinit var homeApi: HomeApi

  fun getParkingLayout(idLevel: String, accessToken: String) {
    subscriptions.add(
        homeApi.getParkingLayout(idLevel, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.getLayoutSuccess(it)
          }
        }, {
          it.message?.let { throwable -> view?.onFailed(throwable) }
        }))
  }

  fun getLevels(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(homeApi.getLevels(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          showProgress(false)
          getLevelsSuccess(it)
        }
      }, {
        showProgress(false)
        it.message?.let { throwable -> onFailed(throwable) }
      }))
    }
  }

  fun addParkingLevel(levelName: String, accessToken: String) {
    view?.showProgressAddLevel(true)
    subscriptions.add(
        homeApi.addParkingLevel(levelName, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.showProgressAddLevel(false)
            view?.addParkingLevelSuccess(it)
          }
        }, {
          view?.showProgressAddLevel(false)
          it.message?.let { throwable -> view?.onFailed(throwable) }
        }))
  }

  fun getSectionDetails(idLevel: String, accessToken: String) {
    subscriptions.add(
        homeApi.getSectionDetails(idLevel, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.getSectionDetailsSuccess(it)
          }
        }, {
          it.message?.let { throwable -> view?.onFailed(throwable) }
        }))
  }

  fun updateParkingSection(idSection: String, accessToken: String) {
    subscriptions.add(
        homeApi.updateParkingSection(idSection, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.updateParkingSectionSuccess(it)
          }
        }, {
          it.message?.let { throwable -> view?.onFailed(throwable) }
        }))
  }

  fun updateLevel(idLevel: String, slotsLayout: String, accessToken: String) {
    subscriptions.add(homeApi.updateLevel(idLevel, slotsLayout, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.updateParkingLayoutSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    }))
  }

  fun editModeParkingLevel(idLevel: String, mode: String, accessToken: String) {
    subscriptions.add(homeApi.editModeParkingLevel(idLevel, mode, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.editModeParkingLevelSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    }))
  }
}