package com.zak.foodmoodfinder.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.FragmentCarboBinding

class CarbFragment : Fragment() {

    private lateinit var viewModel: QuestionnaireViewModel
    private var _binding: FragmentCarboBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(activity as QuestionnaireActivity).apply {
                    setMessage("Apakah anda ingin kembali ke Home?")
                    setPositiveButton("Ya") { _, _ ->
                        (activity as QuestionnaireActivity).finish()
                    }
                    setNegativeButton("Tidak") { _, _ ->
                    }
                    create()
                    show()
                }
            }
        })
        viewModel = activity?.run {
            ViewModelProvider(this)[QuestionnaireViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarboBinding.inflate(inflater, container, false)

        setView()
        setAction()
        return binding.root
    }

    private fun setView() {
        binding.ivCarbo.load(R.drawable.carb) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun setAction() {
        binding.rgCarb.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_carb_0 -> viewModel.postCarb("0")
                R.id.rb_carb_1 -> viewModel.postCarb("1")
                R.id.rb_carb_2 -> viewModel.postCarb("2")
                R.id.rb_carb_3 -> viewModel.postCarb("3")
            }
            binding.btnNextCarb.visibility = View.VISIBLE
        }
        binding.btnNextCarb.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_carbFragment_to_proteinFragment)
        }
    }

}