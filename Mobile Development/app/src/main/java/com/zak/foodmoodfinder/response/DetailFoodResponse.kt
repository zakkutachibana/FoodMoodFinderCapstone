package com.zak.foodmoodfinder.response

import com.google.gson.annotations.SerializedName

data class DetailFoodResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("makanan")
	val makanan: Makanan,

	@field:SerializedName("status")
	val status: Int
)

data class Makanan(

	@field:SerializedName("karbohidrat")
	val karbohidrat: String,

	@field:SerializedName("pengolahan")
	val pengolahan: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("protein")
	val protein: String,

	@field:SerializedName("sayur")
	val sayur: String,

	@field:SerializedName("gambar")
	val gambar: String
)
