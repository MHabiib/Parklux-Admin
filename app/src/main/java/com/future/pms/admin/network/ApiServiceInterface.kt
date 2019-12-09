package com.future.pms.admin.network

import com.future.pms.admin.model.oauth.profile.ParkingZone
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
  @GET("api/parking-zone/detail") fun getParkingZoneDetail(
    @Query("access_token") accessToken: String?
  ): Observable<ParkingZone>
}
