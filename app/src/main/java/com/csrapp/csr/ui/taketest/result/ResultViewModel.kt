package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.ResultRepository

class ResultViewModel(private val resultRepository: ResultRepository) : ViewModel() {

    fun getAptitudeCategories() = resultRepository.getAptitudeCategories()

    fun getAllStreams() = resultRepository.getAllStreams()
}