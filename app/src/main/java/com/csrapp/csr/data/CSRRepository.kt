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

        fun getAptitudeQuestionsByCategory(
            category: String,
            context: Context
        ): List<AptitudeQuestionEntity> {
            val db = CSRDatabase(context)
            return db.aptitudeQuestionDao().getAptitudeQuestionsByCategory(category)
        }

        fun getAllAptitudeQuestions(context: Context): List<AptitudeQuestionEntity> {
            val db = CSRDatabase(context)
            return db.aptitudeQuestionDao().getAllAptitudeQuestions()
        }

        fun getAptitudeCategories(context: Context): List<String> {
            val db = CSRDatabase(context)
            return db.aptitudeQuestionDao().getCategories()
        }
    }
}