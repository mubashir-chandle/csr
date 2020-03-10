package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AptitudeQuestionDao {
    @Query("SELECT * FROM aptitude_question")
    fun getAllAptitudeQuestions(): List<AptitudeQuestionEntity>

    @Query("SELECT * FROM aptitude_question WHERE category=:category")
    fun getAptitudeQuestionsByCategory(category: String): List<AptitudeQuestionEntity>

    @Query("SELECT DISTINCT category FROM aptitude_question")
    fun getAptitudeCategories(): List<String>
}