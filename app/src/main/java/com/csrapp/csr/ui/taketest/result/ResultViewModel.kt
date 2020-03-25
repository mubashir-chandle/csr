package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.ResultRepository

class ResultViewModel(private val resultRepository: ResultRepository) : ViewModel() {

    fun getAllStreams() = resultRepository.getAllStreams()

    fun getAllCategories() = resultRepository.getAllCategories()
}