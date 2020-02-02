package com.future.pms.admin.home.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.home.view.HomeFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HomeModule::class])
interface HomeComponent {
  fun inject(homeFragment: HomeFragment)
}