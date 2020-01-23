package com.future.pms.admin.splash.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.splash.network.SplashApi
import com.future.pms.admin.splash.view.SplashContract
import com.future.pms.admin.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {
  @Inject lateinit var splashApi: SplashApi

  fun isAuthenticated() {
    try {
      if (Authentication.isAuthenticated(view?.isAuthenticated())) {
        view?.onSuccess()
      } else {
        getContext()?.let { Authentication.getRefresh(it) }?.let {
          splashApi.refresh(Constants.GRANT_TYPE_REFRESH, it).subscribeOn(
              Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
            getContext()?.let { context ->
              Authentication.save(context, token)
            }
            view?.onSuccess()
          }, {
            view?.onLogin()
          })
        }?.let {
          subscriptions.add(it)
        }
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      view?.onLogin()
    }
  }
}