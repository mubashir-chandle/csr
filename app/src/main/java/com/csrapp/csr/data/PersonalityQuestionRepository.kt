package com.csrapp.csr.data

class PersonalityQuestionRepository private constructor(private val personalityQuestionDao: PersonalityQuestionDao) {

    fun getQuestions() = personalityQuestionDao.getQuestions()

    companion object {
        private var instance: PersonalityQuestionRepository? = null

        fun getInstance(personalityQuestionDao: PersonalityQuestionDao) =
            instance ?: synchronized(this) {
                instance ?: PersonalityQuestionRepository(personalityQuestionDao).also {
                    instance = it
                }
            }
    }
}