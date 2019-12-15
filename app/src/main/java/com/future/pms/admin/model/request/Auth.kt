package com.future.pms.admin.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize data class Auth(
  @SerializedName("username") val username: String, @SerializedName("password")
  val password: String, @SerializedName("grant_type") val grantType: String
) : Parcelable
