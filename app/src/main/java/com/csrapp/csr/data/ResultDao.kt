package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ResultDao {
    @Query("SELECT DISTINCT category FROM aptitude_question")
    fun getAptitudeCategories(): List<String>

    @Query("SELECT * FROM stream")
    fun getAllStreams(): List<StreamEntity>

    @Query("SELECT title FROM stream WHERE id=:id")
    fun getStreamTitleFromId(id: String): String
}