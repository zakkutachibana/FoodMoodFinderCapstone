package com.zak.foodmoodfinder.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.zak.foodmoodfinder.MainActivity
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.ActivityAuthBinding
import com.zak.foodmoodfinder.databinding.FragmentLoginBinding
import com.zak.foodmoodfinder.utils.FMFUserPreference
import com.zak.foodmoodfinder.utils.ViewModelFactory
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authBinding: ActivityAuthBinding

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setAction()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = FMFUserPreference.getInstance((activity as AuthActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        //IF ERROR IS FALSE THEN INTENT TO MAIN
        viewModel.fmfLoginStatus.observe(viewLifecycleOwner) {
            if (!it.error) {
                MotionToast.createColorToast(
                    activity as AuthActivity,
                    "Selamat Datang!",
                    it.msg,
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity as AuthActivity, R.font.gilroy_regular))
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                (activity as AuthActivity).finish()
            }
        }

        viewModel.let { vm ->
            vm.fmfLoginStatus.observe(viewLifecycleOwner) {
                settingViewModel.setUserPreference(
                    it.user!!.id,
                    it.user.email,
                    it.token
                )
            }
        }
    }


    private fun setAction() {

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.layoutLoginEmail.error = null
            }
        })

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 6 && s.toString().isNotEmpty())
                    binding.layoutLoginPassword.error = getString(R.string.error_password_length)
                else binding.layoutLoginPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.layoutLoginEmail.error = getString(R.string.error_empty_email)
                }
                password.isEmpty() -> {
                    binding.layoutLoginPassword.error = getString(R.string.error_empty_password)
                }
                else -> {
                    viewModel.fmfPostLogin(email, password)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}