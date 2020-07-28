package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AptitudeQuestionDao {
     @Query("SELECT * FROM aptitude_question ORDER BY id")
    fun getAllAptitudeQuestions(): List<AptitudeQuestionEntity>

    @Query("SELECT * FROM aptitude_question WHERE category=:category ORDER BY id")
    fun getAptitudeQuestionsByCategory(category: String): List<AptitudeQuestionEntity>

    @Query("SELECT DISTINCT category FROM aptitude_question ORDER BY category")
    fun getAptitudeCategories(): List<String>
}