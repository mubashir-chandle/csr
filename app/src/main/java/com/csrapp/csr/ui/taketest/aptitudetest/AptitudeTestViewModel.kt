package com.csrapp.csr.ui.taketest.aptitudetest

import android.content.Context
import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.data.AptitudeQuestionRepository

class AptitudeTestViewModel(private val aptitudeQuestionRepository: AptitudeQuestionRepository) :
    ViewModel() {

    private val questions = getRandomQuestions()
    private var spinnerAdapter: SpinnerQuestionAdapter? = null
    var currentQuestionIndex = 0

    fun getSpinnerAdapter(context: Context): SpinnerQuestionAdapter {
        return spinnerAdapter ?: SpinnerQuestionAdapter(context, questions).also {
            this.spinnerAdapter = it
        }
    }

    init {

    }

    fun getAptitudeQuestionsByCategory(category: String) =
        aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)

    fun getAptitudeCategories() = aptitudeQuestionRepository.getAptitudeCategories()

    private fun getRandomQuestions(): List<AptitudeQuestionEntity> {
        val categories = aptitudeQuestionRepository.getAptitudeCategories()

        val questions = mutableListOf<AptitudeQuestionEntity>()
        categories.forEach { category ->
            val categoryQuestions =
                aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)
            val selectedQuestions = categoryQuestions.shuffled().take(1)
            questions.addAll(selectedQuestions)
        }

        return questions.shuffled()
    }
}