package com.future.pms.admin.model.response

import com.google.gson.annotations.SerializedName

data class ListLevel(@SerializedName("idLevel") val idLevel: String, @SerializedName("levelName")
val levelName: String)