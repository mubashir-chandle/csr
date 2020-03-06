package com.csrapp.csr.data

import android.content.Context

class JobDataSource {
    companion object {
        fun getJobs(stream: String, context: Context): List<JobEntity> {
            val db = AppDatabase(context)
            return db.jobDao().getJobsByStream(stream)
        }
    }
}
