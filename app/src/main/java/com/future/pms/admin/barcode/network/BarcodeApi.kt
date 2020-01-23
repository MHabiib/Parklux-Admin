package com.future.pms.admin.barcode.network

import com.future.pms.admin.core.model.response.ParkingZoneResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface BarcodeApi {
  @GET("api2/parking-zone/detail") fun getParkingZoneDetail(@Query("access_token")
  accessToken: String?): Observable<ParkingZoneResponse>

  @GET("api2/qr") fun getQrImage(@Query("access_token")
  accessToken: String?): Observable<ResponseBody>

}