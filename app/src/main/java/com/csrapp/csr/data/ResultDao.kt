package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ResultDao {
    @Query("SELECT * FROM stream")
    fun getAllStreams(): List<StreamEntity>

    @Query("SELECT * FROM aptitude_category")
    fun getAllCategories(): List<AptitudeCategoryEntity>
}