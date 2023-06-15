package com.zak.foodmoodfinder.ui.result

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.ActivityResultBinding
import com.zak.foodmoodfinder.response.FoodsItem
import com.zak.foodmoodfinder.ui.detail.DetailActivity
import com.zak.foodmoodfinder.utils.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel

    companion object{
        const val EXTRA_PREDICTION = "extra_food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@ResultActivity).apply {
                    setMessage("Apakah anda ingin kembali ke Home?")
                    setPositiveButton("Ya") { _, _ ->
                        finish()
                    }
                    setNegativeButton("Tidak") { _, _ ->
                    }
                    create()
                    show()
                }
            }
        })
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val prediction = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_PREDICTION, Predict::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PREDICTION)
        }

        if (prediction != null) {
            setViewModel(prediction)
        }
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvResult.layoutManager = layoutManager

        setAction()
    }
    private fun setViewModel(predict: Predict) {
        resultViewModel = ViewModelProvider(
            this,
            ViewModelFactory(FMFUserPreference.getInstance(dataStore))
        )[ResultViewModel::class.java]

        resultViewModel.postPredictFood(predict.carb.toInt(), predict.protein.toInt(), predict.veggie.toInt(), predict.cooking.toInt())

        resultViewModel.foodStatus.observe(this) {
            if (!it.error) {
                resultViewModel.food.observe(this) { food ->
                    setFoodList(food)
                }
            }
        }
        resultViewModel.error.observe(this) {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        resultViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setAction() {
        binding.btnBack.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage("Apakah anda ingin kembali ke Home?")
                setPositiveButton("Ya") { _, _ ->
                    finish()
                }
                setNegativeButton("Tidak") { _, _ ->
                }
                create()
                show()
            }
        }
    }

    private fun setFoodList(food: List<FoodsItem>) {
        val listFood = ArrayList<FoodsItem>()
        for (foodItem in food) {
            listFood.add(foodItem)
        }
        val adapter = ResultFoodAdapter(listFood)
        binding.rvResult.adapter = adapter

        adapter.setOnItemClickCallback(object : ResultFoodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FoodsItem) {
                showSelectedFoodDetail(data)
            }
        })
    }

    private fun showSelectedFoodDetail(foodItem: FoodsItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_FOOD, foodItem)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressText.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.progressText.visibility = View.GONE
        }
    }
}