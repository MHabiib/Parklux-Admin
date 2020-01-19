package com.future.pms.admin.ui.activitylist

import com.future.pms.admin.ui.base.BasePresenter
import com.future.pms.admin.util.Constants.Companion.ONGOING
import com.future.pms.admin.util.Constants.Companion.PAST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActivityListPresenter @Inject constructor() : BasePresenter<ActivityListContract>() {

  fun findPastBookingParkingZone(accessToken: String, page: Int) {
    val subscribe = api.findPastBookingParkingZone(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.findPastBookingParkingZoneSuccess(it)
      }
    }, {
      it.message?.let { _ -> view?.onFailed(PAST) }
    })
    subscriptions.add(subscribe)
  }

  fun findOngoingBookingParkingZone(accessToken: String, page: Int) {
    val subscribe = api.findOngoingBookingParkingZone(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.findOngoingBookingParkingZoneSuccess(it)
      }
    }, {
      it.message?.let { _ -> view?.onFailed(ONGOING) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ActivityListContract) {
    this.view = view
  }
}