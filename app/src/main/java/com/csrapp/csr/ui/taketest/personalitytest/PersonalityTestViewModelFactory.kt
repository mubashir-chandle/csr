package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.PersonalityQuestionRepository

class PersonalityTestViewModelFactory(private val personalityQuestionRepository: PersonalityQuestionRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PersonalityTestViewModel(personalityQuestionRepository) as T
    }
}