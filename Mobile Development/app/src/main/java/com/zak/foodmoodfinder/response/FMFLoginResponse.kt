package com.zak.foodmoodfinder.response

import com.google.gson.annotations.SerializedName

data class FMFLoginResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String
)

data class User(

	@field:SerializedName("pass")
	val pass: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)
