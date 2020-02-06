package com.future.pms.admin.scan.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.scan.network.ScanApi
import com.future.pms.admin.scan.view.ScanContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScanPresenter @Inject constructor(private val scanApi: ScanApi) :
    BasePresenter<ScanContract>() {

  fun checkoutBookingStepTwo(idSlot: String, fcm: String, accessToken: String) {
    view?.apply {
      subscriptions.add(
          scanApi.checkoutStepTwo(idSlot, fcm, accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            bookingSuccess(it)
          }, {
            onFailed(it.message.toString())
          }))
    }
  }
}