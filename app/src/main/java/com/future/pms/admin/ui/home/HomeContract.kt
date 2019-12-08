package com.future.pms.admin.ui.home

interface HomeContract {
    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
}