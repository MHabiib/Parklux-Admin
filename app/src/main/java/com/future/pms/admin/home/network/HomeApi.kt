package com.future.pms.admin.home.network

import com.future.pms.admin.core.model.response.ListLevel
import com.future.pms.admin.core.model.response.SectionDetails
import io.reactivex.Observable
import retrofit2.http.*

interface HomeApi {
  @GET("api2/parking-zone/{idLevel}/level-layout") fun getParkingLayout(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<String>

  @GET("api2/parking-zone/levels") fun getLevels(@Query("access_token")
  accessToken: String?): Observable<List<ListLevel>>

  @POST("api2/parking-zone/add-level") fun addParkingLevel(@Body levelName: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @GET("api2/parking-zone/{idLevel}/section-details") fun getSectionDetails(@Path("idLevel")
  idLevel: String, @Query("access_token") accessToken: String?): Observable<List<SectionDetails>>

  @POST("api2/parking-zone/update-section") fun updateParkingSection(@Body idSection: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @POST("api2/parking-zone/update-level/{id}") fun updateLevel(@Path("id") idLevel: String, @Body
  slotsLayout: String, @Query("access_token") accessToken: String?): Observable<String>

  @POST("api2/parking-zone/level/edit-mode/{id}/{mode}") fun editModeParkingLevel(@Path("id")
  idLevel: String, @Path("mode") mode: String, @Query("access_token")
  accessToken: String?): Observable<String>

}