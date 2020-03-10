package com.csrapp.csr.data

class AptitudeQuestionRepository private constructor(private val aptitudeQuestionDao: AptitudeQuestionDao) {

    fun getAptitudeQuestionsByCategory(category: String) =
        aptitudeQuestionDao.getAptitudeQuestionsByCategory(category)

    fun getAptitudeCategories() = aptitudeQuestionDao.getAptitudeCategories()

    companion object {
        private var instance: AptitudeQuestionRepository? = null

        fun getInstance(aptitudeQuestionDao: AptitudeQuestionDao) = instance ?: synchronized(this) {
            instance ?: AptitudeQuestionRepository(aptitudeQuestionDao).also { instance = it }
        }
    }
}