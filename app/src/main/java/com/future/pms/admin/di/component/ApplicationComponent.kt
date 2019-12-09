package com.future.pms.admin.di.component

import com.future.pms.admin.BaseApp
import com.future.pms.admin.di.module.ApplicationModule
import dagger.Component

@Component(modules = [ApplicationModule::class]) interface ApplicationComponent {
  fun inject(application: BaseApp)
}