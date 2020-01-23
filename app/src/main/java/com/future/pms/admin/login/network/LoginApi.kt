package com.future.pms.admin.login.network

import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import io.reactivex.Observable
import retrofit2.http.*

interface LoginApi {
  @FormUrlEncoded @POST("oauth/token") fun auth(@Field("username") username: String,
      @Field("password") password: String, @Field("grant_type")
      grantType: String): Observable<Token>

  @GET("api2/parking-zone/detail") fun getParkingZoneDetail(@Query("access_token")
  accessToken: String?): Observable<ParkingZoneResponse>

}