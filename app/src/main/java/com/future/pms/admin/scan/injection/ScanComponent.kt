package com.future.pms.admin.scan.injection

import com.future.pms.admin.core.base.BaseComponent
import com.future.pms.admin.scan.view.ScanFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ScanModule::class])
interface ScanComponent {
  fun inject(scanFragment: ScanFragment)
}