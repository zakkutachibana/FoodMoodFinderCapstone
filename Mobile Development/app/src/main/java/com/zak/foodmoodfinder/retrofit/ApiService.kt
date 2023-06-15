package com.zak.foodmoodfinder.retrofit

import com.zak.foodmoodfinder.response.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun fmfPostLogin(
        @Field("email") email: String,
        @Field("pass") pass: String
    ): Call<FMFLoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun fmfPostRegister(
        @Field("email") email: String,
        @Field("pass") pass: String
    ): Call<FMFStatusResponse>
    @GET("random-foods")
    fun getRandomFood(): Call<FoodResponse>
    @GET("/food-detail/{name}")
    fun getFoodDetail(
        @Path("name") name: String
    ): Call<DetailFoodResponse>

    @FormUrlEncoded
    @POST("predict")
    fun postPredictFood(
        @Field("karbohidrat") karbohidrat: Int,
        @Field("protein") protein: Int,
        @Field("sayur") sayur: Int,
        @Field("pengolahan") pengolahan: Int
        ): Call<FoodResponse>

}