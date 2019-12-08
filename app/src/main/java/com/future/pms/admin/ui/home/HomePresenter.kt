package com.future.pms.admin.ui.home

import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomePresenter @Inject constructor() {
    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = RetrofitClient.create()
    private lateinit var view: HomeContract

    fun attach(view: HomeContract) {
        this.view = view
    }

    fun subscribe() {
        //No implement required
    }
}