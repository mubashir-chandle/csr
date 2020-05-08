package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.BasePersonalityQuestionRepository
import com.csrapp.csr.data.StreamQuestionRepository
import com.csrapp.csr.data.StreamRepository

class PersonalityTestViewModelFactory(
    private val streamRepository: StreamRepository,
    private val basePersonalityQuestionRepository: BasePersonalityQuestionRepository,
    private val streamQuestionRepository: StreamQuestionRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PersonalityTestViewModel(
            streamRepository,
            basePersonalityQuestionRepository,
            streamQuestionRepository
        ) as T
    }
}