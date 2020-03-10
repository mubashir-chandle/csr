package com.csrapp.csr.ui.viewstreams.jobdetail

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.JobRepository

class JobDetailViewModel(private val jobRepository: JobRepository) : ViewModel() {

    fun getJobById(id: Int) = jobRepository.getJobById(id)
}