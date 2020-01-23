package com.future.pms.admin.updatelevel.view

import com.future.pms.admin.core.base.BaseView

interface UpdateLevelContract : BaseView {
  fun updateParkingLevelSuccess(response: String)
}