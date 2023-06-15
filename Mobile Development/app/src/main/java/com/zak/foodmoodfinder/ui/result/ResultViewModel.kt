package com.zak.foodmoodfinder.ui.result

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

class ResultViewModel : ViewModel() {

    companion object{
        private const val TAG = "ResultViewModel"
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _foodStatus = MutableLiveData<FoodResponse>()
    val foodStatus: LiveData<FoodResponse> = _foodStatus

    private val _food = MutableLiveData<List<FoodsItem>>()
    val food: LiveData<List<FoodsItem>> = _food

    val error = MutableLiveData("")

    fun postPredictFood(carb: Int, protein: Int, veggie: Int, cooking: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postPredictFood(carb, protein, veggie, cooking)
        client.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                _isLoading.value = false
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
                _isLoading.value = false
            }
        })
    }
}