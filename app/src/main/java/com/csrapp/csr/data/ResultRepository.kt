package com.csrapp.csr.data

class ResultRepository private constructor(private val resultDao: ResultDao) {

    fun getAllStreams() = resultDao.getAllStreams()

    fun getAllCategories() = resultDao.getAllCategories()

    companion object {
        private var instance: ResultRepository? = null

        fun getInstance(resultDao: ResultDao) = instance ?: synchronized(this) {
            instance ?: ResultRepository(resultDao).also {
                instance = it
            }
        }
    }
}