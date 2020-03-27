package com.csrapp.csr.ui.viewstreams.streamselection

import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.StreamRepository

class StreamSelectionViewModel(private val streamRepository: StreamRepository) : ViewModel() {

    fun getStreams() = streamRepository.getAllStreams()
}