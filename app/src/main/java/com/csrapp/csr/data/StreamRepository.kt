package com.csrapp.csr.data

class StreamRepository private constructor(private val streamDao: StreamDao) {

    fun getAllStreams() = streamDao.getAllStreams()

    companion object {
        private var instance: StreamRepository? = null

        fun getInstance(streamDao: StreamDao) = instance ?: synchronized(this) {
            instance ?: StreamRepository(streamDao).also { instance = it }
        }
    }
}