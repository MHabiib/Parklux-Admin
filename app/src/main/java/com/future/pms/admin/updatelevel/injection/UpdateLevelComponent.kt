package com.future.pms.admin.updatelevel.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.updatelevel.view.UpdateLevelFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [UpdateLevelModule::class])
interface UpdateLevelComponent {
  fun inject(updateLevelFragment: UpdateLevelFragment)
}