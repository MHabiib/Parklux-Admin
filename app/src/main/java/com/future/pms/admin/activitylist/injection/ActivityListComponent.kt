package com.future.pms.admin.activitylist.injection

import com.future.pms.admin.activitylist.view.ActivityListFragment
import com.future.pms.admin.core.base.BaseComponent
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ActivityListModule::class])
interface ActivityListComponent {
  fun inject(activityListFragment: ActivityListFragment)
}