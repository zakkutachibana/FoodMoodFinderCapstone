package com.zak.foodmoodfinder.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "foodName")
    var foodName: String = "",
    @ColumnInfo(name = "photo")
    var photo: String = "",
) : Parcelable
