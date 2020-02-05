package com.future.pms.admin.scan.model

import com.google.gson.annotations.SerializedName

data class CustomerBooking(@SerializedName("dateIn") val dateIn: Long, @SerializedName("dateOut")
val dateOut: Long, @SerializedName("idBooking") val idBooking: String,
    @SerializedName("idParkingZone") val idParkingZone: String, @SerializedName("idSlot")
    val idSlot: String, @SerializedName("idUser") val idUser: String,
    @SerializedName("parkingZoneName") val parkingZoneName: String, val address: String,
    @SerializedName("price") val price: Double, @SerializedName("slotName") val slotName: String,
    @SerializedName("levelName") val levelName: String, @SerializedName("totalTime")
    val totalTime: String, @SerializedName("imageUrl") val imageUrl: String)