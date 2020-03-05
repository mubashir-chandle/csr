package com.csrapp.csr.viewstreams.jobmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.list_item_job.view.*

class JobRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var jobs: ArrayList<Job>
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

    fun populateJobs(streamId: String) {
        jobs = JobDataSource.getJobs(streamId)
    }

    fun setUpNavController(jobNavController: NavController) {
        navController = jobNavController
    }

    class JobViewHolder(itemView: View, val navController: NavController) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val jobTitle = itemView.jobTitle
        private lateinit var job: Job

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(receivedJob: Job) {
            job = receivedJob
            jobTitle.text = receivedJob.title
        }

        override fun onClick(v: View?) {
            val bundle = bundleOf("job" to job)
            navController.navigate(R.id.action_jobSelectionFragment_to_jobDetailFragment, bundle)
        }
    }
}