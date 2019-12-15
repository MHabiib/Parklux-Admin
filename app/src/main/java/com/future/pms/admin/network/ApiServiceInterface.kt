package com.future.pms.admin.network

import com.future.pms.admin.model.profile.ParkingZone
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.SectionDetails
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServiceInterface {
  @GET("api/parking-zone/detail") fun getParkingZoneDetail(
    @Query("access_token") accessToken: String?
  ): Observable<ParkingZone>

  @GET("api/parking-zone/{idLevel}/level-layout") fun getParkingLayout(
    @Path("idLevel") idLevel: String, @Query("access_token") accessToken: String?
  ): Observable<String>

  @GET("api/parking-zone/levels") fun getLevels(
    @Query("access_token") accessToken: String?
  ): Observable<List<ListLevel>>

  @GET("api/parking-zone/{idLevel}/section-details") fun getSectionDetails(
    @Path("idLevel") idLevel: String, @Query("access_token") accessToken: String?
  ): Observable<List<SectionDetails>>

  @POST("api/parking-zone/update-section") fun updateParkingSection(
    @Body idSection: String, @Query("access_token") accessToken: String?
  ): Observable<String>
}
