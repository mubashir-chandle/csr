package com.csrapp.csr.ui.viewstreams.jobselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
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

        initUI()
    }

    private fun initUI() {
        stream = arguments!!.getString("stream")!!

        val factory = InjectorUtils.provideJobSelectionViewModelFactory(requireContext())
        val viewModel = ViewModelProvider(this, factory).get(JobSelectionViewModel::class.java)

        streamTitle.text = viewModel.getStreamTitleById(stream)

        initRecyclerView(stream, viewModel)
    }

    private fun initRecyclerView(stream: String, viewModel: JobSelectionViewModel) {
        val jobAdapter = JobRecyclerAdapter()
        val jobs = viewModel.getJobsByStream(stream)
        jobAdapter.populateJobs(jobs)
        jobAdapter.setUpNavController(navController)

        jobRecyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }
}
