package com.future.pms.admin.ui.updatelevel

import com.future.pms.admin.di.base.BaseView
import retrofit2.Response

interface UpdateLevelContract : BaseView {
  fun updateParkingLevelSuccess(response: Response<Unit>)
}