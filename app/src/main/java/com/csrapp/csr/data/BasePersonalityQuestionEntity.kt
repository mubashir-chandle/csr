package com.csrapp.csr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "base_personality_question")
class BasePersonalityQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "text")
    val text: String?,

    @ColumnInfo(name = "type")
    val type: String?
) {
    enum class PersonalityQuestionType {
        Textual, Slider
    }

    companion object {
        fun getPersonalityQuestionType(basePersonalityQuestionEntity: BasePersonalityQuestionEntity): PersonalityQuestionType {
            return when (basePersonalityQuestionEntity.type) {
                "textual" -> PersonalityQuestionType.Textual
                "slider" -> PersonalityQuestionType.Slider
                else -> throw Exception("Unsupported personality question type")
            }
        }
    }
}