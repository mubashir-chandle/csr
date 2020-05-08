package com.csrapp.csr.data

class BasePersonalityQuestionRepository private constructor(private val basePersonalityQuestionDao: BasePersonalityQuestionDao) {

    fun getQuestionById(id: Int) = basePersonalityQuestionDao.getQuestionById(id)

    companion object {
        private var instance: BasePersonalityQuestionRepository? = null

        fun getInstance(basePersonalityQuestionDao: BasePersonalityQuestionDao) =
            instance ?: synchronized(this) {
                instance ?: BasePersonalityQuestionRepository(basePersonalityQuestionDao).also {
                    instance = it
                }
            }
    }
}