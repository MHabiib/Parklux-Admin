package com.future.pms.admin.di.component

import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.ui.barcode.BarcodeFragment
import com.future.pms.admin.ui.home.HomeFragment
import com.future.pms.admin.ui.profile.ProfileFragment
import dagger.Component

@Component(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(homeFragment: HomeFragment)

    fun inject(barcodeFragment: BarcodeFragment)

    fun inject(profileFragment: ProfileFragment)
}