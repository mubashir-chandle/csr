package com.csrapp.csr.utils

import android.content.Context
import com.csrapp.csr.data.AptitudeQuestionRepository
import com.csrapp.csr.data.CSRDatabase
import com.csrapp.csr.data.JobRepository
import com.csrapp.csr.data.StreamRepository
import com.csrapp.csr.ui.taketest.aptitudetest.AptitudeTestViewModelFactory
import com.csrapp.csr.ui.viewstreams.jobdetail.JobDetailViewModelFactory
import com.csrapp.csr.ui.viewstreams.jobselection.JobSelectionViewModelFactory
import com.csrapp.csr.ui.viewstreams.streamselection.StreamSelectionViewModelFactory

object InjectorUtils {

    fun provideAptitudeTestViewModelFactory(context: Context): AptitudeTestViewModelFactory {
        val repository = AptitudeQuestionRepository.getInstance(
            CSRDatabase.getInstance(context).aptitudeQuestionDao()
        )
        return AptitudeTestViewModelFactory(repository)
    }

    fun provideStreamSelectionViewModelFactory(context: Context): StreamSelectionViewModelFactory {
        val repository = StreamRepository.getInstance(CSRDatabase.getInstance(context).streamDao())
        return StreamSelectionViewModelFactory(repository)
    }

    fun provideJobSelectionViewModelFactory(context: Context): JobSelectionViewModelFactory {
        val repository = JobRepository.getInstance(CSRDatabase.getInstance(context).jobDao())
        return JobSelectionViewModelFactory(repository)
    }

    fun provideJobDetailViewModelFactory(context: Context): JobDetailViewModelFactory {
        val repository = JobRepository.getInstance(CSRDatabase.getInstance(context).jobDao())
        return JobDetailViewModelFactory(repository)
    }
}