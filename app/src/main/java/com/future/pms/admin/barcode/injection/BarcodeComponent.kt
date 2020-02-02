package com.future.pms.admin.barcode.injection

import com.future.pms.admin.barcode.view.BarcodeFragment
import com.future.pms.admin.core.base.BaseComponent
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [BarcodeModule::class])
interface BarcodeComponent {
  fun inject(barcodeFragment: BarcodeFragment)
}