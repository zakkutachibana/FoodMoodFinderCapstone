package com.zak.foodmoodfinder.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zak.foodmoodfinder.MainActivity
import com.zak.foodmoodfinder.databinding.FragmentFavoriteBinding
import com.zak.foodmoodfinder.local.entity.FavoriteEntity
import com.zak.foodmoodfinder.response.FoodsItem
import com.zak.foodmoodfinder.ui.detail.DetailActivity
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.FavoriteFoodAdapter
import com.zak.foodmoodfinder.utils.FavoriteViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteViewModel: FavoriteViewModel
    val favorite = FavoriteEntity()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFavorite.layoutManager = layoutManager

        val pref = FMFUserPreference.getInstance((activity as MainActivity).dataStore)
        val factory = FavoriteViewModelFactory((activity as MainActivity).application, pref)
        favoriteViewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        setViewModel()
        setAction()
        return binding.root
    }

    private fun setViewModel() {
        favoriteViewModel.getFavorite().observe(viewLifecycleOwner) { food ->
            if (food.isNotEmpty()) {
                findFavorite()
            } else {
                binding.empty.visibility = View.VISIBLE
                binding.textFavorite.visibility = View.VISIBLE
                binding.textFavorite2.visibility = View.VISIBLE
            }
        }
    }
    private fun setAction() {
    }
    private fun findFavorite() {
        favoriteViewModel.getFavorite().observe(viewLifecycleOwner) { foods: List<FavoriteEntity>? ->
            val items = arrayListOf<FoodsItem>()
            if (foods != null) {
                foods.map {
                    val item = FoodsItem(it.foodName, it.photo)
                    items.add(item)
                }
                val adapter = FavoriteFoodAdapter(items)
                binding.rvFavorite.adapter = adapter

                adapter.setOnItemClickCallback(object : FavoriteFoodAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: FoodsItem) {
                        showSelectedUserDetail(data)
                    }
                    override fun onRemoveClicked(data: FoodsItem) {
                        favorite.foodName = data.name
                        favorite.photo = data.photo
                        AlertDialog.Builder(activity as MainActivity).apply {
                            setMessage("Apakah anda ingin menghapus ${favorite.foodName} dari Daftar Favorit?")
                            setPositiveButton("Ya") { _, _ ->
                                favoriteViewModel.delete(favorite)
                            }
                            setNegativeButton("Tidak") { _, _ ->
                            }
                            create()
                            show()
                        }
                    }
                })
            }
        }
    }

    private fun showSelectedUserDetail(userItems: FoodsItem) {
        val objectIntent = Intent(context, DetailActivity::class.java)
        objectIntent.putExtra(DetailActivity.EXTRA_FOOD, userItems)
        startActivity(objectIntent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}