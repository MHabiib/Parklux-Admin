package com.future.pms.admin.ui.activitylist

import com.future.pms.admin.di.base.BaseView
import com.future.pms.admin.model.response.ongoingpastbooking.Booking

interface ActivityListContract : BaseView {
  fun findPastBookingParkingZoneSuccess(booking: Booking)

  fun findOngoingBookingParkingZoneSuccess(booking: Booking)
}