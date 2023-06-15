package com.zak.foodmoodfinder.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zak.foodmoodfinder.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * from FavoriteEntity")
    fun getFavorite() : LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteEntity)

    @Delete
    fun delete(favorite: FavoriteEntity)

    @Query("SELECT * from FavoriteEntity WHERE foodName = :foodName")
    fun isFavorite(foodName: String): LiveData<FavoriteEntity>

    @Query("DELETE from FavoriteEntity")
    fun deleteAll()
}