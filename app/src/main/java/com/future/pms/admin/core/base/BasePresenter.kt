package com.future.pms.admin.core.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : BaseView> {
  protected var subscriptions = CompositeDisposable()
  protected var view: V? = null

  fun subscribe() {}

  fun attach(view: V?) {
    this.view = view
  }

  fun detach() {
    view = null
  }

  protected fun getContext(): Context? {
    return when (view) {
      is Fragment -> (view as Fragment).context
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }
}