package com.future.pms.admin.model.oauth.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize data class Refresh(@SerializedName("refresh") val refresh: String) : Parcelable