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

    fun getNumericalImportanceValue() = when (importance) {
        "low" -> 0.3
        "medium" -> 0.5
        "high" -> 0.7
        else -> throw Exception("Question importance must be one of: low, medium, high. Found $importance")
    }

    companion object {
        fun getNumericalImportanceValue(importance: Importance) = when (importance) {
            Importance.Low -> 0.3
            Importance.Medium -> 0.5
            Importance.High -> 0.7
        }
    }

    enum class Importance {
        Low, Medium, High
    }
}
