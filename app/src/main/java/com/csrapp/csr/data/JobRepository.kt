package com.csrapp.csr.data

class JobRepository private constructor(private val jobDao: JobDao) {

    fun getJobsByStream(stream: String) = jobDao.getJobsByStream(stream)
    fun getJobById(id: Int) = jobDao.getJobById(id)

    companion object {
        private var instance: JobRepository? = null

        fun getInstance(jobDao: JobDao) = instance ?: synchronized(this) {
            instance ?: JobRepository(jobDao).also { instance = it }
        }
    }
}