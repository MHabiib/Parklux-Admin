package com.future.pms.admin.scan.network

import com.future.pms.admin.scan.model.CustomerBooking
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScanApi {
  @POST("/api2/booking/checkoutStepTwo/{id}/{fcm}") fun checkoutStepTwo(@Path("id")
  idSlot: String?, @Path("fcm") fcm: String?, @Query("access_token")
  accessToken: String?): Observable<CustomerBooking>
}