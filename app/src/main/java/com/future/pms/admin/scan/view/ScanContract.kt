package com.future.pms.admin.scan.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.scan.model.CustomerBooking

interface ScanContract : BaseView {
  fun showProgress(show: Boolean)
  fun bookingSuccess(customerBooking: CustomerBooking)
}
