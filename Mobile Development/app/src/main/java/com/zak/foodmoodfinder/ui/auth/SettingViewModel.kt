package com.zak.foodmoodfinder.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zak.foodmoodfinder.utils.FMFUserModel
import com.zak.foodmoodfinder.utils.FMFUserPreference
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: FMFUserPreference) : ViewModel() {
    fun setUserPreference(id: Int, email: String, token: String) {
        viewModelScope.launch {
            pref.saveUser(
                FMFUserModel(
                    id,
                    email,
                    token,
                    true
                )
            )
        }
    }
    fun getUser(): LiveData<FMFUserModel> {
        return pref.getUser().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}