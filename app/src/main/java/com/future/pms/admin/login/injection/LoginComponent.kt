package com.future.pms.admin.login.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.login.view.LoginActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [LoginModule::class])
interface LoginComponent {
  fun inject(loginActivity: LoginActivity)
}