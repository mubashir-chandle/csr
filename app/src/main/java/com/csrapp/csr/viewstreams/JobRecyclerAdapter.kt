package com.csrapp.csr.viewstreams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import com.csrapp.csr.data.JobEntity
import kotlinx.android.synthetic.main.list_item_job.view.*

class JobRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var jobs: List<JobEntity>
    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return JobViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_job,
                parent,
                false
            ), navController
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JobViewHolder -> {
                holder.bind(jobs[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    fun populateJobs(streamJobs: List<JobEntity>) {
        jobs = streamJobs
        println("debug: JobRecyclerAdapter: populateJobs: jobs=$jobs")
    }

    fun setUpNavController(jobNavController: NavController) {
        navController = jobNavController
    }

    class JobViewHolder(itemView: View, val navController: NavController) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val jobTitle: TextView = itemView.jobTitle
        private var jobId: Int = 0

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(receivedJob: JobEntity) {
            jobTitle.text = receivedJob.title
            jobId = receivedJob.id!!
        }

        override fun onClick(v: View?) {
            val bundle = bundleOf("jobId" to jobId)
            navController.navigate(R.id.action_jobSelectionFragment_to_jobDetailFragment, bundle)
        }
    }
}