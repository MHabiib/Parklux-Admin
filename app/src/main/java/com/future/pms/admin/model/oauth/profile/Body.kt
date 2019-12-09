package com.future.pms.admin.model.oauth.profile

data class Body(
  val address: String,
  val emailAdmin: String,
  val idParkingZone: String,
  val imageUrl: String,
  val name: String,
  val openHour: String,
  val phoneNumber: String,
  val price: Double
)