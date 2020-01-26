package com.future.pms.admin.login.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.Token

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess(token: Token)
}