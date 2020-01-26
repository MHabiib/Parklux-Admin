package com.future.pms.admin.updatelevel.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.request.LevelDetailsRequest
import com.future.pms.admin.updatelevel.network.UpdateLevelApi
import com.future.pms.admin.updatelevel.view.UpdateLevelContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpdateLevelPresenter @Inject constructor() : BasePresenter<UpdateLevelContract>() {
  @Inject lateinit var updateLevelApi: UpdateLevelApi

  fun updateParkingLevel(accessToken: String, levelDetailsRequest: LevelDetailsRequest) {
    subscriptions.add(
        updateLevelApi.updateParkingLevel(accessToken, levelDetailsRequest).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
          view?.updateParkingLevelSuccess(it)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }
}