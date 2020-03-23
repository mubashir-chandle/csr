package com.csrapp.csr.data

class PersonalityQuestionRepository private constructor(private val personalityQuestionDao: PersonalityQuestionDao) {

    fun getStreams() = personalityQuestionDao.getStreams()

    fun getQuestionsByStream(stream: String, questions: Int) =
        personalityQuestionDao.getQuestionsByStream(stream, questions)

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