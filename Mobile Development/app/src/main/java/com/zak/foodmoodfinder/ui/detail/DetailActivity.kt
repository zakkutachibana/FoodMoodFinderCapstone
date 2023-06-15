package com.zak.foodmoodfinder.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import co.ankurg.expressview.ExpressView
import co.ankurg.expressview.OnCheckListener
import coil.load
import coil.transform.CircleCropTransformation
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.ActivityDetailBinding
import com.zak.foodmoodfinder.local.entity.FavoriteEntity
import com.zak.foodmoodfinder.response.FoodsItem
import com.zak.foodmoodfinder.response.Makanan
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.FavoriteViewModelFactory
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    val favorite = FavoriteEntity()

    companion object {
        const val EXTRA_FOOD = "extra_food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val food = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_FOOD, FoodsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FOOD)
        }

        if (food != null) {
            setViewModel(food)
            setAction(food)
        }
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
    }

    private fun setViewModel(food: FoodsItem) {
        val pref = FMFUserPreference.getInstance(dataStore)
        val factory = FavoriteViewModelFactory(this.application, pref)
        detailViewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        detailViewModel.getFoodDetail(food.name)

        detailViewModel.foodStatus.observe(this) {
            if (!it.error) {
                detailViewModel.food.observe(this) { food ->
                    setView(food)
                    binding.shimmer.visibility = View.GONE
                }
            }
        }
    }

    private fun setView(food: Makanan) {
        binding.tvDetailFoodName.text = food.nama
        binding.tvCarb.text = food.karbohidrat
        binding.tvProtein.text = food.protein
        binding.tvVeggie.text = food.sayur
        binding.tvCooking.text = food.pengolahan
        binding.ivDetailFood.load(food.gambar) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        checkFavorite(food)
    }

    private fun checkFavorite(food: Makanan) {
        detailViewModel.isFavorite(food.nama).observe(this) { favorite ->
            binding.btnFavorite.isChecked = favorite != null
        }
    }

    private fun setAction(food: FoodsItem) {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnFindInGoogle.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=${food.name}"))
            startActivity(browserIntent)
        }

        favorite.photo = food.photo
        favorite.foodName = food.name
        binding.btnFavorite.setOnCheckListener(object : OnCheckListener {
            override fun onChecked(view: ExpressView?) {
                detailViewModel.insert(favorite)
                MotionToast.createColorToast(
                    this@DetailActivity,
                    "Berhasil!",
                    "${food.name} dimasukkan ke Daftar Favorit",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this@DetailActivity, R.font.gilroy_regular)
                )
            }

            override fun onUnChecked(view: ExpressView?) {
                detailViewModel.delete(favorite)
                MotionToast.createColorToast(
                    this@DetailActivity,
                    "Berhasil!",
                    "${food.name} dihapus dari Daftar Favorit",
                    MotionToastStyle.DELETE,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@DetailActivity, R.font.gilroy_regular)
                )
            }
        })


    }

}