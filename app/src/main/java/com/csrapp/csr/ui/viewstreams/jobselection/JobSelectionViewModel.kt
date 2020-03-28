package com.csrapp.csr.ui.viewstreams.jobselection

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.JobRepository
import com.csrapp.csr.data.StreamRepository

class JobSelectionViewModel(
    private val jobRepository: JobRepository,
    private val streamRepository: StreamRepository
) : ViewModel() {

    fun getJobsByStream(stream: String) = jobRepository.getJobsByStream(stream)

    fun getStreamById(id: String) = streamRepository.getStreamById(id)
}