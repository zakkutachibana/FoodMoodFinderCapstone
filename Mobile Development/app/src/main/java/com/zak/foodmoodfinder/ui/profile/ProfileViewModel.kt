package com.zak.foodmoodfinder.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.local.FavoriteRepository

class ProfileViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun deleteAll() = mFavoriteRepository.deleteAll()

}