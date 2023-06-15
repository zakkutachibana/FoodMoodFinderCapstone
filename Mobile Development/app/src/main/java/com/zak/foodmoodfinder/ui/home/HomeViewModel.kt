package com.zak.foodmoodfinder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.response.FoodResponse
import com.zak.foodmoodfinder.response.FoodsItem
import com.zak.foodmoodfinder.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    companion object{
        private const val TAG = "StoryViewModel"
    }

    private val _foodStatus = MutableLiveData<FoodResponse>()
    val foodStatus: LiveData<FoodResponse> = _foodStatus

    private val _food = MutableLiveData<List<FoodsItem>>()
    val food: LiveData<List<FoodsItem>> = _food

    val error = MutableLiveData("")
    fun getRandomFoods() {
        val client = ApiConfig.getApiService().getRandomFood()
        client.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    _foodStatus.postValue(response.body())
                    _food.value = response.body()?.foods
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        error.postValue(errorMessages)
                    }
                }
            }
            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }
        })
    }
}