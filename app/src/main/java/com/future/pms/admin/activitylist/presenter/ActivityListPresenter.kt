package com.future.pms.admin.activitylist.presenter

import com.future.pms.admin.activitylist.network.ActivityListApi
import com.future.pms.admin.activitylist.view.ActivityListContract
import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.util.Constants.Companion.ONGOING
import com.future.pms.admin.util.Constants.Companion.PAST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActivityListPresenter @Inject constructor() : BasePresenter<ActivityListContract>() {
  @Inject lateinit var activityListPresenter: ActivityListApi

  fun findPastBookingParkingZone(accessToken: String, page: Int) {
    subscriptions.add(
        activityListPresenter.findPastBookingParkingZone(accessToken, page).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.findPastBookingParkingZoneSuccess(it)
          }
        }, {
          it.message?.let { _ -> view?.onFailed(PAST) }
        }))
  }

  fun findOngoingBookingParkingZone(accessToken: String, page: Int) {
    subscriptions.add(
        activityListPresenter.findOngoingBookingParkingZone(accessToken, page).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.findOngoingBookingParkingZoneSuccess(it)
          }
        }, {
          it.message?.let { _ -> view?.onFailed(ONGOING) }
        }))
  }

  fun attach(view: ActivityListContract) {
    this.view = view
  }
}