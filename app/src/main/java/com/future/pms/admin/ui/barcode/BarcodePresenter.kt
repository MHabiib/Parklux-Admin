package com.future.pms.admin.ui.barcode

import javax.inject.Inject

class BarcodePresenter @Inject constructor() {
    private lateinit var view: BarcodeContract

    fun attach(view: BarcodeContract) {
        this.view = view
    }
}