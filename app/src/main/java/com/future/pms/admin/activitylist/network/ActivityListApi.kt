package com.future.pms.admin.activitylist.network

import com.future.pms.admin.core.model.response.ongoingpastbooking.Booking
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityListApi {
  @GET("/api2/booking/past/parking-zone") fun findPastBookingParkingZone(@Query("access_token")
  accessToken: String?, @Query("page") page: Int?): Observable<Booking>

  @GET("/api2/booking/ongoing/parking-zone") fun findOngoingBookingParkingZone(
      @Query("access_token") accessToken: String?, @Query("page") page: Int?): Observable<Booking>
}