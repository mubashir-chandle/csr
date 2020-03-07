package com.csrapp.csr.ui.viewstreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.csrapp.csr.R
import com.csrapp.csr.data.CSRDatabase
import com.csrapp.csr.data.JobEntity
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

        jobId = arguments!!.getInt("jobId")

        val db = CSRDatabase(activity!!)
        job = db.jobDao().getJobById(jobId)[0]

        jobTitle.text = job.title
        jobDescription.text = job.description
    }
}
