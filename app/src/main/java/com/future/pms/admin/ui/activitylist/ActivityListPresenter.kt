package com.future.pms.admin.ui.activitylist

import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActivityListPresenter @Inject constructor() {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: ActivityListContract
    private val api: ApiServiceInterface = RetrofitClient.create()

    fun findPastBookingParkingZone(accessToken: String, page: Int) {
        val subscribe = api.findPastBookingParkingZone(accessToken, page).subscribeOn(
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (null != it) {
                view.findPastBookingParkingZoneSuccess(it)
            }
        }, {
            it.message?.let { it1 -> view.findPastBookingParkingZoneFailed(it1) }
        })
        subscriptions.add(subscribe)
    }

    fun findOngoingBookingParkingZone(accessToken: String, page: Int) {
        val subscribe = api.findOngoingBookingParkingZone(accessToken, page).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (null != it) {
                view.findOngoingBookingParkingZoneSuccess(it)
            }
        }, {
            it.message?.let { it1 -> view.findOngoingBookingParkingZoneFailed(it1) }
        })
        subscriptions.add(subscribe)
    }

    fun attach(view: ActivityListContract) {
        this.view = view
    }
}