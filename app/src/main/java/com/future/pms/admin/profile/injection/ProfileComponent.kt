package com.future.pms.admin.profile.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.profile.view.ProfileFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ProfileModule::class])
interface ProfileComponent {
  fun inject(profileFragment: ProfileFragment)
}