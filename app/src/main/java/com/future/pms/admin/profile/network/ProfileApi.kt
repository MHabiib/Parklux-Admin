package com.future.pms.admin.profile.network

import com.future.pms.admin.core.model.response.ParkingZoneResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileApi {
  @GET("api2/parking-zone/detail") fun getParkingZoneDetail(@Query("access_token")
  accessToken: String?): Observable<ParkingZoneResponse>

  @Multipart @PUT("api2/parking-zone/update-zone") fun updateParkingZone(@Query("access_token")
  accessToken: String?, @Part("parkingZone")
  parkingZone: ParkingZoneResponse): Observable<ParkingZoneResponse>

  @Multipart @PUT("api2/parking-zone/update-zone/picture") fun updateParkingZonePicture(
      @Query("access_token") accessToken: String?, @Part
      picture: MultipartBody.Part): Observable<String>

}