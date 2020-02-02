package com.future.pms.admin.maps.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.maps.view.MapsActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [MapsModule::class])
interface MapsComponent {
  fun inject(mapsActivity: MapsActivity)
}