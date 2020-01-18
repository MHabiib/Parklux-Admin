package com.future.pms.admin.ui.login

import com.future.pms.admin.di.base.BaseView

interface LoginContract : BaseView {
  fun onSuccess()
  fun onAuthorized()
}