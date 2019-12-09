package com.future.pms.admin

import android.app.Application
import com.future.pms.admin.di.component.ApplicationComponent
import com.future.pms.admin.di.component.DaggerApplicationComponent
import com.future.pms.admin.di.module.ApplicationModule

class BaseApp : Application() {

  lateinit var component: ApplicationComponent

  companion object {
    lateinit var instance: BaseApp private set
  }

  override fun onCreate() {
    super.onCreate()

    instance = this
    setup()
  }

  private fun setup() {
    component = DaggerApplicationComponent.builder().applicationModule(
      ApplicationModule(this)
    ).build()
    component.inject(this)
  }
}
