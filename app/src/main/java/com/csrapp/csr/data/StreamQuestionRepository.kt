package com.csrapp.csr.data

class StreamQuestionRepository private constructor(private val streamQuestionDao: StreamQuestionDao) {

    fun getQuestionsByStream(stream: String) = streamQuestionDao.getQuestionsByStream(stream)

    companion object {
        private var instance: StreamQuestionRepository? = null

        fun getInstance(streamQuestionDao: StreamQuestionDao) =
            instance ?: synchronized(this) {
                instance ?: StreamQuestionRepository(streamQuestionDao).also {
                    instance = it
                }
            }
    }
}
