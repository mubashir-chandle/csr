package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.ResultRepository

class ResultViewModelFactory(private val resultRepository: ResultRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResultViewModel(resultRepository) as T
    }
}