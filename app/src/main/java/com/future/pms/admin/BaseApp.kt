package com.future.pms.admin

import android.app.Application
import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.core.base.BaseModule
import com.future.pms.admin.core.base.DaggerBaseComponent
import timber.log.Timber

class BaseApp : Application() {

  lateinit var baseComponent: BaseComponent

  companion object {
    lateinit var instance: BaseApp
  }

  override fun onCreate() {
    super.onCreate()
    baseComponent = DaggerBaseComponent.builder().baseModule(BaseModule(this)).build()
    baseComponent.inject(this)
    instance = this

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
