package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BasePersonalityQuestionDao {
    @Query("SELECT * FROM base_personality_question WHERE id=:id")
    fun getQuestionById(id: Int): BasePersonalityQuestionEntity
}