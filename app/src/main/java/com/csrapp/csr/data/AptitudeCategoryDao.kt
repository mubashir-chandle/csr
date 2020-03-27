package com.csrapp.csr.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AptitudeCategoryDao {
    @Query("SELECT * FROM aptitude_category")
    fun getAllCategories(): List<AptitudeCategoryEntity>
}