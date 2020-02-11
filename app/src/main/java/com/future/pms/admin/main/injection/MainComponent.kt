package com.future.pms.admin.main.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.main.view.MainActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class])
interface MainComponent {
  fun inject(mainActivity: MainActivity)
}