package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream ORDER BY title")
    fun getAllStreams(): List<StreamEntity>

    @Query("SELECT * FROM stream WHERE id=:id")
    fun getStreamById(id: String): StreamEntity
}