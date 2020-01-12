package com.future.pms.admin.network

import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.model.request.LevelDetailsRequest
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.ParkingZoneResponse
import com.future.pms.admin.model.response.SectionDetails
import com.future.pms.admin.model.response.ongoingpastbooking.Booking
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceInterface {
  @GET("api2/parking-zone/detail") fun getParkingZoneDetail(@Query("access_token")
  accessToken: String?): Observable<ParkingZone>

  @GET("api2/parking-zone/{idLevel}/level-layout") fun getParkingLayout(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<String>

  @GET("api2/parking-zone/levels") fun getLevels(@Query("access_token")
  accessToken: String?): Observable<List<ListLevel>>

  @GET("api2/parking-zone/{idLevel}/section-details") fun getSectionDetails(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<List<SectionDetails>>

  @POST("api2/parking-zone/update-section") fun updateParkingSection(@Body idSection: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @POST("api2/parking-zone/update-level/{id}") fun updateLevel(@Path("id") idLevel: String, @Body
  slotsLayout: String, @Query("access_token") accessToken: String?): Observable<String>

  @POST("api2/parking-zone/add-level") fun addParkingLevel(@Body levelName: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @POST("api2/parking-zone/level/edit-mode/{id}/{mode}") fun editModeParkingLevel(@Path("id")
  idLevel: String, @Path("mode") mode: String, @Query("access_token")
  accessToken: String?): Observable<String>

  @Multipart @PUT("api2/parking-zone/update-zone") fun updateParkingZone(@Query("access_token")
  accessToken: String?, @Part("parkingZone")
  parkingZone: ParkingZoneResponse): Observable<ParkingZoneResponse>

  @Multipart @PUT("api2/parking-zone/update-zone/picture") fun updateParkingZonePicture(
      @Query("access_token") accessToken: String?, @Part
      picture: MultipartBody.Part): Observable<String>

  @PUT("api2/parking-zone/update-level") fun updateParkingLevel(@Query("access_token")
  accessToken: String?, @Body levelDetailsRequest: LevelDetailsRequest): Observable<Response<Unit>>

  @GET("api2/qr") fun getQrImage(@Query("access_token")
  accessToken: String?): Observable<ResponseBody>

  @GET("/api2/booking/past/parking-zone") fun findPastBookingParkingZone(@Query("access_token")
  accessToken: String?, @Query("page") page: Int?): Observable<Booking>

  @GET("/api2/booking/ongoing/parking-zone") fun findOngoingBookingParkingZone(
      @Query("access_token") accessToken: String?, @Query("page") page: Int?): Observable<Booking>
}
