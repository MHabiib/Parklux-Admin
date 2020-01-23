package com.future.pms.admin.splash.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.splash.view.SplashActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [SplashModule::class])
interface SplashComponent {
  fun inject(splashActivity: SplashActivity)
}