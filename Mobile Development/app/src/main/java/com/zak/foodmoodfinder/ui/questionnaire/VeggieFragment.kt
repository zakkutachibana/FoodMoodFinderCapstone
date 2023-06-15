package com.zak.foodmoodfinder.ui.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.databinding.FragmentVeggieBinding

class VeggieFragment : Fragment() {

    private lateinit var viewModel: QuestionnaireViewModel
    private var _binding: FragmentVeggieBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view?.findNavController()?.popBackStack()
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
        _binding = FragmentVeggieBinding.inflate(inflater, container, false)

        setAction()
        setView()
        return binding.root
    }

    private fun setView() {
        binding.ivVeggie.load(R.drawable.veggie) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun setAction() {
        binding.rgVeggie.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_veggie_0 -> viewModel.postVeggie("0")
                R.id.rb_veggie_1 -> viewModel.postVeggie("1")
            }
            binding.btnNextVeggie.visibility = View.VISIBLE
        }
        binding.btnNextVeggie.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_veggieFragment_to_cookingFragment)
        }
        binding.btnPrevVeggie.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }

    }

}