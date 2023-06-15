package com.zak.foodmoodfinder.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserModel (
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
    )

data class FMFUserModel (
    val id: Int,
    val email: String,
    val token: String,
    val isLogin: Boolean
)
@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,
    val lon: Double?
) : Parcelable

@Parcelize
data class Predict(
    val carb: String,
    val protein: String,
    val veggie: String,
    val cooking: String
) : Parcelable