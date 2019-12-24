package com.future.pms.admin.ui.activitylist

import com.future.pms.admin.model.response.ongoingpastbooking.Booking

interface ActivityListContract {
    fun findPastBookingParkingZoneSuccess(booking: Booking)

    fun findPastBookingParkingZoneFailed(response: String)
}