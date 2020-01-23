package com.future.pms.admin.updatelevel.network

import com.future.pms.admin.core.model.request.LevelDetailsRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Query

interface UpdateLevelApi {
  @PUT("api2/parking-zone/update-level") fun updateParkingLevel(@Query("access_token")
  accessToken: String?, @Body levelDetailsRequest: LevelDetailsRequest): Observable<String>
}