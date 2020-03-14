package com.csrapp.csr.ui.taketest.aptitudetest

import com.csrapp.csr.data.AptitudeQuestionEntity

class QuestionHolder(
    val question: AptitudeQuestionEntity,
    var responseType: QuestionResponseType,
    var optionSelected: Int? = null,
    var confidence: Int? = null
) {
    enum class QuestionResponseType {
        UNANSWERED, MARKED, ANSWERED
    }
}
