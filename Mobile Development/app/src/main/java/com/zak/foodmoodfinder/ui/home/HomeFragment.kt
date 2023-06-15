package com.zak.foodmoodfinder.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.zak.foodmoodfinder.MainActivity
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.FragmentHomeBinding
import com.zak.foodmoodfinder.response.FoodsItem
import com.zak.foodmoodfinder.ui.auth.SettingViewModel
import com.zak.foodmoodfinder.ui.detail.DetailActivity
import com.zak.foodmoodfinder.ui.questionnaire.QuestionnaireActivity
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.FoodAdapter
import com.zak.foodmoodfinder.utils.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val helper = LinearSnapHelper()
        binding.rvFoodHome.layoutManager = layoutManager
        helper.attachToRecyclerView(binding.rvFoodHome)

        setViewModel()
        setAction()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()

        homeViewModel.getRandomFoods()

        homeViewModel.foodStatus.observe(viewLifecycleOwner) {
            if (!it.error) {
                homeViewModel.food.observe(viewLifecycleOwner) { food ->
                    setFoodList(food)
                    binding.shimmer.visibility = View.GONE
                }
            }
        }
        homeViewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViewModel() {
        val pref = FMFUserPreference.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.tvGreetingName.text = getString(R.string.greeting, user.email)
                binding.ivProfilePicture.load(R.drawable.fmf_logo) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else {
                binding.tvGreetingName.text = getString(R.string.greeting, getString(R.string.guest))
                binding.ivProfilePicture.load(R.drawable.fmf_logo) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    private fun setAction() {
        binding.btnQuestionnaire.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setFoodList(food: List<FoodsItem>) {
        val listFood = ArrayList<FoodsItem>()
        for (foodItem in food) {
            listFood.add(foodItem)
        }
        val adapter = FoodAdapter(listFood)
        binding.rvFoodHome.adapter = adapter

        adapter.setOnItemClickCallback(object : FoodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FoodsItem) {
                showSelectedFoodDetail(data)
            }
        })
    }

    private fun showSelectedFoodDetail(foodItem: FoodsItem) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_FOOD, foodItem)
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}