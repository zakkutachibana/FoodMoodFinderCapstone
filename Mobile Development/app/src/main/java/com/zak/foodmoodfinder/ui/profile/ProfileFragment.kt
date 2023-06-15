package com.zak.foodmoodfinder.ui.profile

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
import coil.load
import com.zak.foodmoodfinder.MainActivity
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.FragmentProfileBinding
import com.zak.foodmoodfinder.ui.auth.AuthActivity
import com.zak.foodmoodfinder.ui.auth.SettingViewModel
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.FavoriteViewModelFactory
import com.zak.foodmoodfinder.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val pref = FMFUserPreference.getInstance((activity as MainActivity).dataStore)
        val factory = FavoriteViewModelFactory((activity as MainActivity).application, pref)
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        setViewModel()
        setAction()
        return binding.root
    }

    private fun setViewModel() {
        val pref = FMFUserPreference.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.ivProfilePicture.load(R.drawable.default_profile)
                binding.tvProfileName.text = user.email
                binding.tvProfileEmail.text = user.email
            } else {
                binding.tvProfileName.text = getString(R.string.guest)
                binding.tvProfileEmail.text = getString(R.string.guest)
                binding.btnAuth.text = getString(R.string.title_login)
            }
        }
    }

    private fun setAction() {
        val pref = FMFUserPreference.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.btnAuth.setOnClickListener {
                    AlertDialog.Builder(activity as MainActivity).apply {
                        setMessage("Apakah Anda yakin ingin Logout? \nJika anda Logout, daftar Favorit anda akan dihapus!")
                        setPositiveButton("Ya") { _, _ ->
                            settingViewModel.logout()
                            profileViewModel.deleteAll()
                        }
                        setNegativeButton("Tidak") { _, _ ->
                        }
                        create()
                        show()
                    }
                }
                binding.btnEdit.setOnClickListener {
                    AlertDialog.Builder(activity as MainActivity).apply {
                        setMessage("Maaf, fitur masih dalam tahap Pengembangan.")
                        setPositiveButton("Ok") { _, _ ->
                        }
                        create()
                        show()
                    }
                }
            } else {
                binding.btnAuth.setOnClickListener {
                    val intent = Intent(context, AuthActivity::class.java)
                    startActivity(intent)
                }
                binding.btnEdit.setOnClickListener {
                    AlertDialog.Builder(activity as MainActivity).apply {
                        setMessage("Anda belum Login!")
                        setPositiveButton("Ok") { _, _ ->
                        }
                        create()
                        show()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}