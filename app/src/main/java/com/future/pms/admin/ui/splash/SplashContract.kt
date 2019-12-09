package com.future.pms.admin.ui.splash

import android.content.Context

interface SplashContract {
  fun onSuccess()
  fun onLogin()
  fun onError(e: Throwable)
  fun isAuthenticated(): Context?
  fun refreshFetcher()
}