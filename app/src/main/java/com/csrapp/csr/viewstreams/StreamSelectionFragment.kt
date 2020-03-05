package com.csrapp.csr.viewstreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.viewstreams.streamselection.SpacingItemDecoration
import com.csrapp.csr.viewstreams.streamselection.StreamDataSource
import com.csrapp.csr.viewstreams.streamselection.StreamRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_stream_selection.*

class StreamSelectionFragment : Fragment() {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val streamAdapter = StreamRecyclerAdapter()
        val streams = StreamDataSource.getStreamData()
        streamAdapter.populateWithData(streams)
        streamAdapter.setUpNavController(navController)

        streamRecyclerView.apply {
            adapter = streamAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpacingItemDecoration(32))
        }
    }
}
