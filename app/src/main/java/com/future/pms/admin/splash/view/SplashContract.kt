package com.future.pms.admin.splash.view

import android.content.Context
import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.Token

interface SplashContract : BaseView {
  fun onSuccess(token: Token)
  fun onLogin()
  fun isAuthenticated(): Context?
  fun refreshToken()
}