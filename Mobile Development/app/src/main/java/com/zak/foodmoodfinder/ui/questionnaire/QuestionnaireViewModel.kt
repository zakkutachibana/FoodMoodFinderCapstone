package com.zak.foodmoodfinder.ui.questionnaire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zak.foodmoodfinder.utils.Predict

class QuestionnaireViewModel : ViewModel() {

    val carbohydrate = MutableLiveData<String>()
    val protein = MutableLiveData<String>()
    val vegetable = MutableLiveData<String>()
    val cooking = MutableLiveData<String>()

    private val _prediction = MutableLiveData<Predict>()
    val prediction: LiveData<Predict> = _prediction

    fun postCarb(carb: String) {
        carbohydrate.value = carb
    }
    fun postProtein(proteins: String) {
        protein.value = proteins
    }
    fun postVeggie(veggie: String) {
        vegetable.value = veggie
    }
    fun postCooking(cook: String) {
        cooking.value = cook
    }

    fun postPrediction() {
        _prediction.value = Predict(
            carbohydrate.value.toString(), protein.value.toString(), vegetable.value.toString(), cooking.value.toString()
        )
    }

}
