package com.future.pms.admin.di.component

import com.future.pms.admin.di.module.ActivityModule
import com.future.pms.admin.ui.login.LoginActivity
import com.future.pms.admin.ui.main.MainActivity
import com.future.pms.admin.ui.splash.SplashActivity
import dagger.Component

@Component(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(loginActivity: LoginActivity)
}