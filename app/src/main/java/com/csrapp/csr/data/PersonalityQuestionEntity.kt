package com.csrapp.csr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personality_question")
class PersonalityQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "stream")
    val stream: String?,

    @ColumnInfo(name = "text")
    val text: String?,

    @ColumnInfo(name = "type")
    val type: String?
)