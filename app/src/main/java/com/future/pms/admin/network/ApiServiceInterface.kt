package com.future.pms.admin.network

import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.model.request.LevelDetailsRequest
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.model.response.SectionDetails
import com.future.pms.admin.model.response.ongoingpastbooking.Booking
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceInterface {
  @GET("api/parking-zone/detail") fun getParkingZoneDetail(@Query("access_token")
  accessToken: String?): Observable<ParkingZone>

  @GET("api/parking-zone/{idLevel}/level-layout") fun getParkingLayout(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<String>

  @GET("api/parking-zone/levels") fun getLevels(@Query("access_token")
  accessToken: String?): Observable<List<ListLevel>>

  @GET("api/parking-zone/{idLevel}/section-details") fun getSectionDetails(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<List<SectionDetails>>

  @POST("api/parking-zone/update-section") fun updateParkingSection(@Body idSection: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @POST("api/parking-zone/update-level/{id}") fun updateLevel(@Path("id") idLevel: String, @Body
  slotsLayout: String, @Query("access_token") accessToken: String?): Observable<String>

  @POST("api/parking-zone/add-level") fun addParkingLevel(@Body levelName: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @POST("api/parking-zone/level/edit-mode/{id}/{mode}") fun editModeParkingLevel(@Path("id")
  idLevel: String, @Path("mode") mode: String, @Query("access_token")
  accessToken: String?): Observable<String>

  @Multipart @PUT("api/parking-zone/update-zone") fun updateParkingZone(@Query("access_token")
  accessToken: String?, @Part("parkingZone")
  parkingZone: ParkingZoneResponse): Observable<ParkingZoneResponse>

  @PUT("api/parking-zone/update-level") fun updateParkingLevel(@Query("access_token")
  accessToken: String?, @Body levelDetailsRequest: LevelDetailsRequest): Observable<Response<Void>>

  @GET("api/qr") fun getQrImage(@Query("access_token") accessToken: String?): Observable<String>

  @GET("/api/booking/past/parking-zone") fun findPastBookingParkingZone(@Query("access_token")
  accessToken: String?, @Query("page") page: Int?): Observable<Booking>

  @GET("/api/booking/ongoing/parking-zone") fun findOngoingBookingParkingZone(@Query("access_token")
  accessToken: String?, @Query("page") page: Int?): Observable<Booking>
}
