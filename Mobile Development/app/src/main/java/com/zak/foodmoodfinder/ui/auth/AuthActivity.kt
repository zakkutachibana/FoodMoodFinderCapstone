package com.zak.foodmoodfinder.ui.auth

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.utils.SectionsPagerAdapter
import com.zak.foodmoodfinder.databinding.ActivityAuthBinding
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.ViewModelFactory
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_login,
            R.string.title_register
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Tab Layout
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 1

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        setView()
        setViewModel()
    }

    private fun setView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        binding.ivLogo.load(R.drawable.fmf_logo) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(FMFUserPreference.getInstance(dataStore))
        )[AuthViewModel::class.java]

        viewModel.error.observe(this) {
            if (it.isNotEmpty()) {
                MotionToast.createColorToast(
                    this,
                    "Autentikasi Gagal!",
                    it,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_regular))
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}