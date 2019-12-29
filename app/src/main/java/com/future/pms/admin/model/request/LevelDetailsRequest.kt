package com.future.pms.admin.model.request

import com.google.gson.annotations.SerializedName

data class LevelDetailsRequest(@SerializedName("idLevel") val idLevel: String,
    @SerializedName("levelName") val levelName: String, @SerializedName("status")
    val status: String)