package com.future.pms.admin.home.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.home.network.HomeApi
import com.future.pms.admin.home.view.HomeContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor(private val homeApi: HomeApi) :
    BasePresenter<HomeContract>() {

  fun getParkingLayout(idLevel: String, accessToken: String) {
    subscriptions.add(
        homeApi.getParkingLayout(idLevel, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.getLayoutSuccess(it)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }

  fun getLevels(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(homeApi.getLevels(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        getLevelsSuccess(it)
      }, {
        showProgress(false)
        view?.onFailed(it.message.toString())
      }))
    }
  }

  fun addParkingLevel(levelName: String, accessToken: String) {
    view?.showProgressAddLevel(true)
    subscriptions.add(
        homeApi.addParkingLevel(levelName, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.showProgressAddLevel(false)
          view?.addParkingLevelSuccess(it)
        }, {
          view?.showProgressAddLevel(false)
          view?.onFailed(it.message.toString())
        }))
  }

  fun getSectionDetails(idLevel: String, accessToken: String) {
    subscriptions.add(
        homeApi.getSectionDetails(idLevel, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.getSectionDetailsSuccess(it)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }

  fun updateParkingSection(idSection: String, accessToken: String) {
    subscriptions.add(
        homeApi.updateParkingSection(idSection, accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.updateParkingSectionSuccess(it)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }

  fun updateLevel(idLevel: String, slotsLayout: String, accessToken: String) {
    subscriptions.add(homeApi.updateLevel(idLevel, slotsLayout, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.updateParkingLayoutSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
    }))
  }

  fun editModeParkingLevel(idLevel: String, mode: String, accessToken: String) {
    subscriptions.add(homeApi.editModeParkingLevel(idLevel, mode, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.editModeParkingLevelSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
    }))
  }
}