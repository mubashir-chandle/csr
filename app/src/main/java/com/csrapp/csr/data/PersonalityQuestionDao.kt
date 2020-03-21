package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PersonalityQuestionDao {
    @Query("SELECT * FROM personality_question")
    fun getQuestions(): List<PersonalityQuestionEntity>

    @Query("SELECT DISTINCT stream FROM personality_question ORDER BY stream")
    fun getStreams(): List<String>
}