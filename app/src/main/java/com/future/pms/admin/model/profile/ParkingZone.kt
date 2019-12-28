package com.future.pms.admin.model.profile

import com.google.gson.annotations.SerializedName

data class ParkingZone(@SerializedName("body") val body: Body, @SerializedName("headers")
val headers: Headers, @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusCodeValue") val statusCodeValue: Int)