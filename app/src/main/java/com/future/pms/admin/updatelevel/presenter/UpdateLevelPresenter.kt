package com.future.pms.admin.updatelevel.presenter

import com.future.pms.admin.R
import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.request.LevelDetailsRequest
import com.future.pms.admin.updatelevel.network.UpdateLevelApi
import com.future.pms.admin.updatelevel.view.UpdateLevelContract
import com.future.pms.admin.util.Constants.Companion.DELETE_LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.LEVEL_AVAILABLE
import com.future.pms.admin.util.Constants.Companion.LEVEL_TAKE_OUT
import com.future.pms.admin.util.Constants.Companion.LEVEL_UNAVAILABLE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpdateLevelPresenter @Inject constructor() : BasePresenter<UpdateLevelContract>() {
  @Inject lateinit var updateLevelApi: UpdateLevelApi

  fun updateParkingLevel(accessToken: String, idLevel: String, levelName: String, status: Int) {
    view?.showProgress(true)
    val statusStr: String = when (status) {
      DELETE_LEVEL_STATUS -> LEVEL_TAKE_OUT
      R.id.rb_available -> LEVEL_AVAILABLE
      else -> LEVEL_UNAVAILABLE
    }
    val levelDetailsRequest = LevelDetailsRequest(idLevel, levelName, statusStr)
    subscriptions.add(
        updateLevelApi.updateParkingLevel(accessToken, levelDetailsRequest).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
          view?.showProgress(false)
          view?.updateParkingLevelSuccess(it)
        }, {
          view?.showProgress(false)
          it.message?.let { _ -> view?.onFailed(it.toString()) }
        }))
  }
}