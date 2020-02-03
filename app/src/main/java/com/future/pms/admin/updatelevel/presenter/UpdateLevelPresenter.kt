package com.future.pms.admin.updatelevel.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.request.LevelDetailsRequest
import com.future.pms.admin.updatelevel.network.UpdateLevelApi
import com.future.pms.admin.updatelevel.view.UpdateLevelContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpdateLevelPresenter @Inject constructor(private val updateLevelApi: UpdateLevelApi) :
    BasePresenter<UpdateLevelContract>() {

  fun updateParkingLevel(accessToken: String, levelDetailsRequest: LevelDetailsRequest) {
    view?.showProgress(true)
    subscriptions.add(
        updateLevelApi.updateParkingLevel(accessToken, levelDetailsRequest).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
          view?.showProgress(false)
          view?.updateParkingLevelSuccess(it)
        }, {
          view?.showProgress(false)
          view?.onFailed(it.message.toString())
        }))
  }
}