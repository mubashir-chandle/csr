package com.csrapp.csr.ui.viewstreams.jobselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.JobRepository

class JobSelectionViewModelFactory(private val jobRepository: JobRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        return JobSelectionViewModel(jobRepository) as T
    }
}