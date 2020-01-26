package com.future.pms.admin.splash.presenter

import com.future.pms.admin.core.base.BasePresenter
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.splash.network.SplashApi
import com.future.pms.admin.splash.view.SplashContract
import com.future.pms.admin.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {
  @Inject lateinit var splashApi: SplashApi

  fun refreshToken(refreshToken: String) {
    subscriptions.add(splashApi.refresh(Constants.GRANT_TYPE_REFRESH, refreshToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      view?.onSuccess(token)
    }, {
      view?.onLogin()
    }))
  }
}