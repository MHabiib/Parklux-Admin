package com.future.pms.admin.di.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.admin.model.Token
import com.future.pms.admin.network.*
import com.future.pms.admin.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BasePresenter<V : BaseView> {
  protected val api: ApiServiceInterface = RetrofitClient.create()
  protected var subscriptions = CompositeDisposable()
  protected var view: V? = null

  fun subscribe() {}

  protected fun getContext(): Context? {
    return when (view) {
      is Fragment -> (view as Fragment).context
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }

  protected fun refreshFetcher(functionOnSuccess: () -> Unit, functionOnFailed: () -> Unit) {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe = getContext()?.let { Authentication.getRefresh(it) }?.let {
      authFetcher.refresh(NetworkConstant.GRANT_TYPE_REFRESH, it).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
        getContext()?.let { context -> Authentication.save(context, token) }
        functionOnSuccess()
      }, {
        functionOnFailed()
      })
    }
    subscribe?.let { subscriptions.add(it) }
  }
}