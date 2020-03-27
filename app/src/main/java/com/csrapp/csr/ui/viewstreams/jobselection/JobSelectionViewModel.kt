package com.csrapp.csr.ui.viewstreams.jobselection

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.JobRepository

class JobSelectionViewModel(private val jobRepository: JobRepository) : ViewModel() {

    fun getJobsByStream(stream: String) = jobRepository.getJobsByStream(stream)

    fun getStreamTitleById(id: String) = jobRepository.getStreamTitleById(id)
}