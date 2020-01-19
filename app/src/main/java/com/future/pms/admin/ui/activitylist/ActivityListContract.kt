package com.future.pms.admin.ui.activitylist
import com.future.pms.admin.model.response.ongoingpastbooking.Booking
import com.future.pms.admin.ui.base.BaseView

interface ActivityListContract : BaseView {
  fun findPastBookingParkingZoneSuccess(booking: Booking)

  fun findOngoingBookingParkingZoneSuccess(booking: Booking)
}