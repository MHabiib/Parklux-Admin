package com.future.pms.admin.ui.updatelevel

import com.future.pms.admin.R
import com.future.pms.admin.model.request.LevelDetailsRequest
import com.future.pms.admin.ui.base.BasePresenter
import com.future.pms.admin.util.Constants.Companion.DELETE_LEVEL_STATUS
import com.future.pms.admin.util.Constants.Companion.LEVEL_AVAILABLE
import com.future.pms.admin.util.Constants.Companion.LEVEL_TAKE_OUT
import com.future.pms.admin.util.Constants.Companion.LEVEL_UNAVAILABLE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpdateLevelPresenter @Inject constructor() : BasePresenter<UpdateLevelContract>() {

  fun updateParkingLevel(accessToken: String, idLevel: String, levelName: String, status: Int) {
    val statusStr: String = when (status) {
      DELETE_LEVEL_STATUS -> LEVEL_TAKE_OUT
      R.id.rb_available -> LEVEL_AVAILABLE
      else -> LEVEL_UNAVAILABLE
    }
    val levelDetailsRequest = LevelDetailsRequest(idLevel, levelName, statusStr)
    val subscribe = api.updateParkingLevel(accessToken, levelDetailsRequest).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        if (it.raw().code() == 400) {
          view?.onFailed(it.message())
        } else {
          view?.updateParkingLevelSuccess(it)
        }
      }
    }, {
      it.message?.let { _ -> view?.onFailed(it.toString()) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: UpdateLevelContract) {
    this.view = view
  }
}