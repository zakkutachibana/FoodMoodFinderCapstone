package com.zak.foodmoodfinder.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.response.FMFLoginResponse
import com.zak.foodmoodfinder.response.FMFStatusResponse
import com.zak.foodmoodfinder.retrofit.ApiConfig
import com.zak.foodmoodfinder.utils.FMFUserPreference
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: FMFUserPreference) : ViewModel() {
    companion object{
        private const val TAG = "LoginViewModel"
    }

    private val _fmfLoginStatus = MutableLiveData<FMFLoginResponse>()
    val fmfLoginStatus: LiveData<FMFLoginResponse> = _fmfLoginStatus

    private val _fmfRegisterStatus = MutableLiveData<FMFStatusResponse>()
    val fmfRegisterStatus: LiveData<FMFStatusResponse> = _fmfRegisterStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData("")

    fun fmfPostRegister(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService2().fmfPostRegister(email, password)
        client.enqueue(object : Callback<FMFStatusResponse> {
            override fun onResponse(call: Call<FMFStatusResponse>, response: Response<FMFStatusResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _fmfRegisterStatus.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("msg")
                        error.postValue(errorMessages)
                    }
                }
            }
            override fun onFailure(call: Call<FMFStatusResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }
    fun fmfPostLogin(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService2().fmfPostLogin(email, password)
        client.enqueue(object : Callback<FMFLoginResponse> {
            override fun onResponse(call: Call<FMFLoginResponse>, response: Response<FMFLoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _fmfLoginStatus.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("msg")
                        error.postValue(errorMessages)
                    }
                }
            }
            override fun onFailure(call: Call<FMFLoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

}