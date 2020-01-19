package com.future.pms.admin.ui.splash

import android.content.Context
import com.future.pms.admin.ui.base.BaseView

interface SplashContract : BaseView {
  fun onSuccess()
  fun onLogin()
  fun isAuthenticated(): Context?
  fun refreshFetcher()
}