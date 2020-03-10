package com.csrapp.csr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aptitude_question")
data class AptitudeQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "option_1")
    val option1: String,

    @ColumnInfo(name = "option_2")
    val option2: String,

    @ColumnInfo(name = "option_3")
    val option3: String,

    @ColumnInfo(name = "option_4")
    val option4: String,

    @ColumnInfo(name = "correct_option")
    val correctOption: Int,

    @ColumnInfo(name = "reference_image")
    val referenceImage: String?
)
