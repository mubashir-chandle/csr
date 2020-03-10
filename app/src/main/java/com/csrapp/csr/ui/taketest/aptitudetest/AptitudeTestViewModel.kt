package com.csrapp.csr.ui.taketest.aptitudetest

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.AptitudeQuestionRepository

class AptitudeTestViewModel(private val aptitudeQuestionRepository: AptitudeQuestionRepository) :
    ViewModel() {

    fun getAptitudeQuestionsByCategory(category: String) =
        aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)

    fun getAptitudeCategories() = aptitudeQuestionRepository.getAptitudeCategories()

}