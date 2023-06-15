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
import com.zak.foodmoodfinder.databinding.FragmentProteinBinding

class ProteinFragment : Fragment() {

    private lateinit var viewModel: QuestionnaireViewModel
    private var _binding: FragmentProteinBinding? = null
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
        _binding = FragmentProteinBinding.inflate(inflater, container, false)

        setAction()
        setView()
        return binding.root
    }
    private fun setView() {
        binding.ivProtein.load(R.drawable.protein) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }
    private fun setAction() {
        binding.rgProtein.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_protein_0 -> viewModel.postProtein("0")
                R.id.rb_protein_1 -> viewModel.postProtein("1")
                R.id.rb_protein_2 -> viewModel.postProtein("2")
                R.id.rb_protein_3 -> viewModel.postProtein("3")
                R.id.rb_protein_4 -> viewModel.postProtein("4")
            }
            binding.btnNextProtein.visibility = View.VISIBLE
        }
        binding.btnNextProtein.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_proteinFragment_to_veggieFragment)
        }
        binding.btnPrevProtein.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }

    }

}