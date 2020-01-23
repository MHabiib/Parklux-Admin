package com.future.pms.admin.splash.view

import android.content.Context
import com.future.pms.admin.core.base.BaseView

interface SplashContract : BaseView {
  fun onSuccess()
  fun onLogin()
  fun isAuthenticated(): Context?
}