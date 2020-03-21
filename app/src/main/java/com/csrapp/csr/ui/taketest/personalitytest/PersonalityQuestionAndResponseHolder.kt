package com.csrapp.csr.ui.taketest.personalitytest

import com.csrapp.csr.data.PersonalityQuestionEntity

class PersonalityQuestionAndResponseHolder(
    val question: PersonalityQuestionEntity,
    var responseString: String? = null,
    var responseValue: Int? = null
)
