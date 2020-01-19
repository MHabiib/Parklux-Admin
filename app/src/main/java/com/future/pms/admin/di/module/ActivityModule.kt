package com.future.pms.admin.di.module

import android.app.Activity
import com.future.pms.admin.ui.login.LoginPresenter
import com.future.pms.admin.ui.main.MainPresenter
import com.future.pms.admin.ui.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module class ActivityModule(private var activity: Activity) {
  @Provides fun provideActivity(): Activity {
    return activity
  }

  @Provides fun providePresenter(): MainPresenter {
    return MainPresenter()
  }

  @Provides fun provideLoginPresenter(): LoginPresenter {
    return LoginPresenter()
  }

  @Provides fun provideSplashPresenter(): SplashPresenter {
    return SplashPresenter()
  }
}