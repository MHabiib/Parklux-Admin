package com.future.pms.admin.di.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

open class BasePresenter<V : BaseMVPView> {
    protected var view: V? = null

    protected fun call(view: V, function: () -> Unit) {
        when (view) {
            is Fragment -> (view as Fragment).activity?.runOnUiThread { function() }
            is Activity -> (view as Activity).runOnUiThread { function() }
        }
    }

    protected fun <T> call(view: V, parameter: T, function: (parameter: T) -> Unit) {
        when (view) {
            is Fragment -> (view as Fragment).activity?.runOnUiThread { function(parameter) }
            is Activity -> (view as Activity).runOnUiThread { function(parameter) }
        }
    }

    protected fun getContext(): Context? {
        return when (view) {
            is Fragment -> (view as Fragment).context
            is Activity -> (view as Activity)
            else -> throw Exception()
        }
    }
}