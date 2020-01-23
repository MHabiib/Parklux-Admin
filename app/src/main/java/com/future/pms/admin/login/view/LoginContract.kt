package com.future.pms.admin.login.view

import com.future.pms.admin.core.base.BaseView

interface LoginContract : BaseView {
  fun onSuccess()
  fun onAuthorized()
}