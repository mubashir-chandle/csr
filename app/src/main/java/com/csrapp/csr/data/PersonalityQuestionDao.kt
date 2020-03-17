package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PersonalityQuestionDao {
    @Query("SELECT * FROM personality_question")
    fun getQuestions(): List<PersonalityQuestionEntity>
}