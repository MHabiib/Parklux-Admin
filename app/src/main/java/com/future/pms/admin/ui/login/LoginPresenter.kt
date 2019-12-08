package com.future.pms.admin.ui.login

import com.future.pms.admin.di.base.BasePresenter
import com.future.pms.admin.model.oauth.Token
import com.future.pms.admin.network.APICreator
import com.future.pms.admin.network.AuthAPI
import com.future.pms.admin.network.NetworkConstant.GRANT_TYPE
import com.future.pms.admin.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract>() {
    private val subscriptions = CompositeDisposable()

    fun subscribe() {}

    fun attach(view: LoginContract) {
        this.view = view
    }

    fun login(username: String, password: String) {
        val authFetcher = APICreator(AuthAPI::class.java).generate()
        val subscribe = authFetcher.auth(username, password, GRANT_TYPE).subscribeOn(
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
            getContext()?.let { Authentication.save(it, token) }
            view?.let { view -> call(view, view::onSuccess) }
        }, { view?.onError() })
        subscriptions.add(subscribe)
    }
}