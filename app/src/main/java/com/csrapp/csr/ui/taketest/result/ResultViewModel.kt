package com.csrapp.csr.ui.taketest.result

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.AptitudeCategoryRepository
import com.csrapp.csr.data.StreamRepository

class ResultViewModel(
    private val aptitudeCategoryRepository: AptitudeCategoryRepository,
    private val streamRepository: StreamRepository
) : ViewModel() {

    fun getAllStreams() = streamRepository.getAllStreams()

    fun getAllCategories() = aptitudeCategoryRepository.getAllCategories()
}