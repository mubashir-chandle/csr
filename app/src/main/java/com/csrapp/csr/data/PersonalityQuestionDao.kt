package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PersonalityQuestionDao {
    @Query("SELECT DISTINCT stream FROM personality_question ORDER BY stream")
    fun getStreams(): List<String>

    @Query("SELECT * FROM personality_question WHERE stream=:stream LIMIT :questions")
    fun getQuestionsByStream(stream: String, questions: Int): List<PersonalityQuestionEntity>
}