package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream")
    fun getAllStreams(): List<StreamEntity>

    @Query("SELECT title FROM stream WHERE id=:id")
    fun getStreamTitleById(id: String): String
}