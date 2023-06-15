package com.zak.foodmoodfinder.response

import com.google.gson.annotations.SerializedName

data class FMFStatusResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("msg")
	val msg: String
)
