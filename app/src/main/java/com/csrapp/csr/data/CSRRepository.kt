package com.csrapp.csr.data

import android.content.Context

class CSRRepository {
    companion object {
        fun getStreamData(context: Context): List<StreamEntity> {
            val db = CSRDatabase(context)
            return db.streamDao().getAllStreams()
        }

        fun getJobs(stream: String, context: Context): List<JobEntity> {
            val db = CSRDatabase(context)
            return db.jobDao().getJobsByStream(stream)
        }
    }
}