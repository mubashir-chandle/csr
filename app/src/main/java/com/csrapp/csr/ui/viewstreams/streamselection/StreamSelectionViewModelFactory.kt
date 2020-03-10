package com.csrapp.csr.ui.viewstreams.streamselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.data.StreamRepository

class StreamSelectionViewModelFactory(private val streamRepository: StreamRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StreamSelectionViewModel(streamRepository) as T
    }
}