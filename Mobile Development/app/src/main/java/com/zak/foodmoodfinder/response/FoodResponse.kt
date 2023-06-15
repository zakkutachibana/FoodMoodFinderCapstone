package com.zak.foodmoodfinder.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FoodResponse(

	@field:SerializedName("foods")
	val foods: List<FoodsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

@Parcelize
data class FoodsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String
): Parcelable
