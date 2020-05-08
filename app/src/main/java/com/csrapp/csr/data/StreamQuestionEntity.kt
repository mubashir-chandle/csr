package com.csrapp.csr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream_question")
class StreamQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "base_question_id")
    val baseQuestionId: Int,

    @ColumnInfo(name = "stream")
    val stream: String,

    @ColumnInfo(name = "importance")
    val importance: String
) {

    fun getImportanceValue() = when (importance) {
        "low" -> Importance.Low
        "medium" -> Importance.Medium
        "high" -> Importance.High
        else -> throw Exception("Question importance must be one of: low, medium, high. Found $importance")
    }

    enum class Importance {
        Low, Medium, High
    }
}
