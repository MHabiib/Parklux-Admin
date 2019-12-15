package com.future.pms.admin.model.response

import com.google.gson.annotations.SerializedName

data class SectionDetails(
  @SerializedName("idSection") val idSection: String, @SerializedName("sectionName")
  val sectionName: String, @SerializedName("status") val status: String,
  @SerializedName("totalDisableSlot") val totalDisableSlot: Int, @SerializedName("totalEmptySlot")
  val totalEmptySlot: Int, @SerializedName("totalTakenSlot") val totalTakenSlot: Int
)