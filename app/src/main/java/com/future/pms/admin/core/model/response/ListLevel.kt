package com.future.pms.admin.core.model.response

import com.google.gson.annotations.SerializedName

data class ListLevel(@SerializedName("idLevel") val idLevel: String, @SerializedName("levelName")
val levelName: String, @SerializedName("levelStatus")
val levelStatus: String)