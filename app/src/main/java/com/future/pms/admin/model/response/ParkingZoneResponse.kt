package com.future.pms.admin.model.response

import com.google.gson.annotations.SerializedName

data class ParkingZoneResponse(@SerializedName("address") val address: String,
    @SerializedName("emailAdmin") val emailAdmin: String, @SerializedName("name") val name: String,
    @SerializedName("openHour") val openHour: String, @SerializedName("password")
    val password: String, @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("price") val price: Double)