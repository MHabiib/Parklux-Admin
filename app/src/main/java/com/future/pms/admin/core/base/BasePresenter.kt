package com.future.pms.admin.core.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : BaseView> {
  protected var subscriptions = CompositeDisposable()
  protected var view: V? = null

  fun subscribe() {}

  fun attach(view: V?) {
    this.view = view
  }

  fun detach() {
    subscriptions.dispose()
    view = null
  }
}