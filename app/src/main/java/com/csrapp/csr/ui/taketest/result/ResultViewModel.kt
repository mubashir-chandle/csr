package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.ResultRepository

class ResultViewModel(private val resultRepository: ResultRepository) : ViewModel() {

    fun getAptitudeCategories() = resultRepository.getAptitudeCategories()

    fun getStreamTitleFromId(id: String) = resultRepository.getStreamTitleFromId(id)

    fun getAllStreams() = resultRepository.getAllStreams()
}