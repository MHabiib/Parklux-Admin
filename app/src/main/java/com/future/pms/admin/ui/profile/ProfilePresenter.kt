package com.future.pms.admin.ui.profile

import javax.inject.Inject

class ProfilePresenter @Inject constructor() {
    private lateinit var view: ProfileContract

    fun attach(view: ProfileContract) {
        this.view = view
    }
}