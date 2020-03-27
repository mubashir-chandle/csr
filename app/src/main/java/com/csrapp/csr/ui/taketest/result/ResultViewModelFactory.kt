package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.AptitudeCategoryRepository
import com.csrapp.csr.data.StreamRepository

class ResultViewModelFactory(
    private val aptitudeCategoryRepository: AptitudeCategoryRepository,
    private val streamRepository: StreamRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResultViewModel(aptitudeCategoryRepository, streamRepository) as T
    }
}