package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StreamQuestionDao {
     @Query("SELECT * FROM stream_question WHERE stream=:stream ORDER BY id")
    fun getQuestionsByStream(stream: String): List<StreamQuestionEntity>
}