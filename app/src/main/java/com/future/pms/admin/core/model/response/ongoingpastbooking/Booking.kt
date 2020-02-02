package com.future.pms.admin.core.model.response.ongoingpastbooking

import com.google.gson.annotations.SerializedName

data class Booking(@SerializedName("content") val content: List<Content>, @SerializedName("empty")
val empty: Boolean, @SerializedName("first") val first: Boolean, @SerializedName("last")
val last: Boolean, @SerializedName("number") val number: Int, @SerializedName("numberOfElements")
val numberOfElements: Int, @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("size") val size: Int, @SerializedName("sort") val sort: Sort,
    @SerializedName("totalElements") val totalElements: Int, @SerializedName("totalPages")
    val totalPages: Int)