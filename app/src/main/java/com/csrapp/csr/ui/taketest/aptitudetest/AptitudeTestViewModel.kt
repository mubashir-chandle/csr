package com.csrapp.csr.ui.taketest.aptitudetest

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.data.AptitudeQuestionRepository

class AptitudeTestViewModel(private val aptitudeQuestionRepository: AptitudeQuestionRepository) :
    ViewModel() {

    fun getAptitudeQuestionsByCategory(category: String) =
        aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)

    fun getAptitudeCategories() = aptitudeQuestionRepository.getAptitudeCategories()

    fun getRandomQuestions(): List<AptitudeQuestionEntity> {
        val categories = aptitudeQuestionRepository.getAptitudeCategories()

        val questions = mutableListOf<AptitudeQuestionEntity>()
        categories.forEach { category ->
            val categoryQuestions =
                aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)
            val selectedQuestions = categoryQuestions.shuffled().take(5)
            questions.addAll(selectedQuestions)
        }

        return questions.shuffled()
    }
}