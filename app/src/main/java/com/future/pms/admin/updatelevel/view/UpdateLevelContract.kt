package com.future.pms.admin.updatelevel.view

import com.future.pms.admin.core.base.BaseView
import retrofit2.Response

interface UpdateLevelContract : BaseView {
  fun updateParkingLevelSuccess(response: Response<Unit>)
}