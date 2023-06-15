package com.zak.foodmoodfinder.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.FragmentRegisterBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setAction()
        setViewModel()
        return binding.root
    }

    private fun setViewModel() {

        viewModel.fmfRegisterStatus.observe(viewLifecycleOwner) {
            if (!it.error) {
                MotionToast.createColorToast(
                    activity as AuthActivity,
                    "Registrasi Berhasil!",
                    it.msg,
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity as AuthActivity, R.font.gilroy_regular))
                binding.edRegisterEmail.setText("")
                binding.edRegisterPassword.setText("")
                binding.edRegisterConfirmPassword.setText("")
                binding.layoutRegisterEmail.error = null
                binding.layoutRegisterPassword.error = null
                binding.layoutRegisterConfirmPassword.error = null
            }
        }

//        viewModel.error.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                MotionToast.createColorToast(
//                    activity as AuthActivity,
//                    "Registrasi Gagal!",
//                    it,
//                    MotionToastStyle.ERROR,
//                    MotionToast.GRAVITY_BOTTOM,
//                    MotionToast.LONG_DURATION,
//                    ResourcesCompat.getFont(activity as AuthActivity, R.font.gilroy_regular))            }
//        }

    }

    private fun setAction() {

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.layoutRegisterEmail.error = null
            }
        })

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 6 && s.toString().isNotEmpty())
                    binding.layoutRegisterPassword.error = getString(R.string.error_password_length)
                else binding.layoutRegisterPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterConfirmPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != binding.edRegisterPassword.text.toString())
                    binding.layoutRegisterConfirmPassword.error = getString(R.string.error_password_not_match)
                else binding.layoutRegisterConfirmPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRegister.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterConfirmPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.layoutRegisterEmail.error = getString(R.string.error_empty_email)
                }
                password.isEmpty() -> {
                    binding.layoutRegisterPassword.error = getString(R.string.error_empty_password)
                }
                confirmPassword.isEmpty() -> {
                    binding.layoutRegisterConfirmPassword.error = getString(R.string.error_empty_confirm_password)
                }
                password != confirmPassword -> {
                    binding.layoutRegisterPassword.error = getString(R.string.error_password_not_match)
                    binding.layoutRegisterConfirmPassword.error = getString(R.string.error_password_not_match)
                }
                else -> {
//                    viewModel.postRegister(name, email, password)
                    viewModel.fmfPostRegister(email, password)
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}