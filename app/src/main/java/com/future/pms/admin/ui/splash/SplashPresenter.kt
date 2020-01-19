package com.future.pms.admin.ui.splash

import com.future.pms.admin.ui.base.BasePresenter
import com.future.pms.admin.util.Authentication
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {

  fun unsubscribe() {
    subscriptions.clear()
  }

  fun attach(view: SplashContract) {
    this.view = view
  }

  fun isAuthenticated() {
    view?.apply {
      try {
        if (Authentication.isAuthenticated(isAuthenticated())) {
          onSuccess()
        } else {
          refreshFetcher()
        }
      } catch (e: Authentication.WithoutAuthenticatedException) {
        onLogin()
      }
    }
  }
}