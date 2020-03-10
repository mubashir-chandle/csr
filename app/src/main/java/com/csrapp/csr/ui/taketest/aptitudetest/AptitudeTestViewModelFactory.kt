package com.csrapp.csr.ui.taketest.aptitudetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.AptitudeQuestionRepository

class AptitudeTestViewModelFactory(private val aptitudeQuestionRepository: AptitudeQuestionRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AptitudeTestViewModel(aptitudeQuestionRepository) as T
    }
}