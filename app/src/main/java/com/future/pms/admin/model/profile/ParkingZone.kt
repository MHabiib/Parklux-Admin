package com.future.pms.admin.model.profile

import com.future.pms.admin.model.response.ParkingZoneResponse
import com.google.gson.annotations.SerializedName

data class ParkingZone(
    @SerializedName("body") val parkingZoneResponse: ParkingZoneResponse,
    @SerializedName("statusCode") val statusCode: String, @SerializedName("statusCodeValue") val statusCodeValue: Int
)