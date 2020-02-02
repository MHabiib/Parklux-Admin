package com.future.pms.admin.activitylist.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.response.ongoingpastbooking.Booking

interface ActivityListContract : BaseView {
  fun findPastBookingParkingZoneSuccess(booking: Booking)

  fun findOngoingBookingParkingZoneSuccess(booking: Booking)
}