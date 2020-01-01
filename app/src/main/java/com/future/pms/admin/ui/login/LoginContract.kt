package com.future.pms.admin.ui.login

import com.future.pms.admin.di.base.BaseMVPView

interface LoginContract : BaseMVPView {
  fun onSuccess()
  fun onAuthorized()
  fun onFailed(e: String)
  fun onError()
}