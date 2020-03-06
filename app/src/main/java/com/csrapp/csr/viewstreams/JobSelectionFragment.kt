package com.csrapp.csr.viewstreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.data.JobDataSource
import kotlinx.android.synthetic.main.fragment_job_selection.*

class JobSelectionFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var stream: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        stream = arguments!!.getString("stream")!!
        initRecyclerView(stream)
        println("debug: JobSelectionFragment: stream=$stream")
    }

    private fun initRecyclerView(stream: String) {
        val jobAdapter = JobRecyclerAdapter()
        val jobs = JobDataSource.getJobs(stream, activity!!)
        jobAdapter.populateJobs(jobs)
        jobAdapter.setUpNavController(navController)

        jobRecyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }
}
