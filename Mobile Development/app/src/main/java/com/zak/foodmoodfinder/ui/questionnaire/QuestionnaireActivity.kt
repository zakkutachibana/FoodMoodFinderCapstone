package com.zak.foodmoodfinder.ui.questionnaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.zak.foodmoodfinder.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setAction()
    }
    private fun setAction() {
        binding.btnBack.setOnClickListener{
            binding.btnBack.setOnClickListener {
                AlertDialog.Builder(this).apply {
                    setMessage("Apakah anda ingin kembali ke Home?")
                    setPositiveButton("Ya") { _, _ ->
                        finish()
                    }
                    setNegativeButton("Tidak") { _, _ ->
                    }
                    create()
                    show()
                }
            }
        }
    }
}