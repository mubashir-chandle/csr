package com.csrapp.csr.viewstreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.csrapp.csr.R
import com.csrapp.csr.viewstreams.jobmodel.Job
import kotlinx.android.synthetic.main.fragment_job_detail.*

class JobDetailFragment : Fragment() {
    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job = arguments!!.getParcelable<Job>("job")!!
        jobTitle.text = job.title
        jobDescription.text = job.description
    }
}
