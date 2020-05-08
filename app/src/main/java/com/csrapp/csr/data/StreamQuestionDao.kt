package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StreamQuestionDao {
    @Query("SELECT * FROM stream_question WHERE stream=:stream")
    fun getQuestionsByStream(stream: String): List<StreamQuestionEntity>
}