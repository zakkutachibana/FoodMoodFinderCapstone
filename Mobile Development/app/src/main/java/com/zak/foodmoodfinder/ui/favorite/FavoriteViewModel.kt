package com.zak.foodmoodfinder.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.local.FavoriteRepository
import com.zak.foodmoodfinder.local.entity.FavoriteEntity

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getFavorite()  : LiveData<List<FavoriteEntity>> = mFavoriteRepository.getFavorite()

    fun delete(favorite: FavoriteEntity) {
        mFavoriteRepository.delete(favorite)
    }

}