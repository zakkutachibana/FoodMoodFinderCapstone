package com.zak.foodmoodfinder.ui.questionnaire

import android.content.Intent
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
import com.zak.foodmoodfinder.databinding.FragmentCookingBinding
import com.zak.foodmoodfinder.ui.result.ResultActivity
import com.zak.foodmoodfinder.utils.Predict

class CookingFragment : Fragment() {

    private lateinit var viewModel: QuestionnaireViewModel
    private var _binding: FragmentCookingBinding? = null
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
        _binding = FragmentCookingBinding.inflate(inflater, container, false)

        setView()
        setAction()
        return binding.root
    }

    private fun setView() {
        binding.ivCooking.load(R.drawable.cooking) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun setAction() {
        binding.btnPrevCooking.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
        binding.rgCooking.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_cooking_0 -> viewModel.postCooking("0")
                R.id.rb_cooking_1 -> viewModel.postCooking("1")
                R.id.rb_cooking_2 -> viewModel.postCooking("2")
                R.id.rb_cooking_3 -> viewModel.postCooking("3")
            }
            binding.btnNextCooking.visibility = View.VISIBLE
            viewModel.postPrediction()
        }
        viewModel.prediction.observe(viewLifecycleOwner) {
            predictFoods(it)
        }
    }

    private fun predictFoods(predict: Predict) {
        binding.btnNextCooking.setOnClickListener {
            val intent = Intent(activity, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_PREDICTION, predict)
            startActivity(intent)
            (activity as QuestionnaireActivity).finish()
        }
    }

}