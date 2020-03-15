package com.csrapp.csr.ui.viewstreams.jobdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csrapp.csr.R
import com.csrapp.csr.data.JobEntity
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_job_detail.*

class JobDetailFragment : Fragment() {
    private var jobId: Int = 0
    private lateinit var job: JobEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideJobDetailViewModelFactory(requireContext())
        val viewModel = ViewModelProvider(this, factory).get(JobDetailViewModel::class.java)

        jobId = arguments!!.getInt("jobId")

        job = viewModel.getJobById(jobId)[0]

        jobTitle.text = job.title
        jobDescription.text = job.description
    }
}
