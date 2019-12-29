package com.future.pms.admin.ui.updatelevel

import retrofit2.Response

interface UpdateLevelContract {
  fun updateParkingLevelSuccess(response: Response<Void>)
  fun showErrorMessage()
}