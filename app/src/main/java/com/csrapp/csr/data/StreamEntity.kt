package com.csrapp.csr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
data class StreamEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    val description: String
)
