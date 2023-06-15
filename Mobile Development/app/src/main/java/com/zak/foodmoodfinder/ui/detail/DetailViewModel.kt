package com.zak.foodmoodfinder.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.local.FavoriteRepository
import com.zak.foodmoodfinder.local.entity.FavoriteEntity
import com.zak.foodmoodfinder.response.DetailFoodResponse
import com.zak.foodmoodfinder.response.Makanan
import com.zak.foodmoodfinder.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    companion object{
        private const val TAG = "DetailViewModel"
    }

    private val _foodStatus = MutableLiveData<DetailFoodResponse>()
    val foodStatus: LiveData<DetailFoodResponse> = _foodStatus

    private val _food = MutableLiveData<Makanan>()
    val food: LiveData<Makanan> = _food

    val error = MutableLiveData("")
    fun getFoodDetail(foodName: String) {
        val client = ApiConfig.getApiService().getFoodDetail(foodName)
        client.enqueue(object : Callback<DetailFoodResponse> {
            override fun onResponse(call: Call<DetailFoodResponse>, response: Response<DetailFoodResponse>) {
                if (response.isSuccessful) {
                    _foodStatus.postValue(response.body())
                    _food.value = response.body()?.makanan
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        error.postValue(errorMessages)
                    }
                }
            }
            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }
        })
    }

    fun insert(favorite: FavoriteEntity) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: FavoriteEntity) {
        mFavoriteRepository.delete(favorite)
    }

    fun isFavorite(foodName: String) : LiveData<FavoriteEntity> = mFavoriteRepository.isFavorite(foodName)
}