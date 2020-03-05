package com.csrapp.csr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.streams.SpacingItemDecoration
import com.csrapp.csr.streams.StreamDataSource
import com.csrapp.csr.streams.StreamRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_stream_selection.*

class StreamSelectionFragment : Fragment(), View.OnClickListener {
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
        view.findViewById<Button>(R.id.btnViewJobs).setOnClickListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val streamAdapter = StreamRecyclerAdapter()
        val streams = StreamDataSource.getStreamData()
        streamAdapter.populateWithData(streams)

        streamRecyclerView.apply {
            adapter = streamAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpacingItemDecoration(32))
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnViewJobs -> navController.navigate(R.id.action_streamSelectionFragment_to_jobSelectionFragment)
        }
    }
}
