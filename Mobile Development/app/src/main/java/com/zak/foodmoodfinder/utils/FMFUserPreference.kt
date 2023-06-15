package com.zak.foodmoodfinder.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FMFUserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<FMFUserModel> {
        return dataStore.data.map { preferences ->
            FMFUserModel(
                preferences[ID_KEY] ?: 1,
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: FMFUserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FMFUserPreference? = null

        private val ID_KEY = intPreferencesKey("id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): FMFUserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = FMFUserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}