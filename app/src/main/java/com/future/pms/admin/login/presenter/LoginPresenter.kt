package com.future.pms.admin.login.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.login.network.LoginApi
import com.future.pms.admin.login.view.LoginContract
import com.future.pms.admin.util.Constants.Companion.GRANT_TYPE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract>() {
  @Inject lateinit var loginApi: LoginApi

  fun login(username: String, password: String) {
    subscriptions.add(loginApi.auth(username, password, GRANT_TYPE).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      getContext()?.let { Authentication.save(it, token) }
      view?.onSuccess()
    }, { view?.onFailed(it.toString()) }))
  }

  fun loadData(accessToken: String) {
    subscriptions.add(
        loginApi.getParkingZoneDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
          view?.onAuthorized()
        }, {
          getContext()?.let {
            Authentication.delete(it)
            view?.onFailed(it.toString())
          }
        }))
  }
}