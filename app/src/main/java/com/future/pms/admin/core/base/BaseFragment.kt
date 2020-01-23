package com.future.pms.admin.core.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseView {

  private var parentActivity: BaseActivity? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is BaseActivity) {
      val activity = context as BaseActivity?
      this.parentActivity = activity
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(false)
  }
}