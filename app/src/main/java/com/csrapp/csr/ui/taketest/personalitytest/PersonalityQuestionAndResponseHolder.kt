package com.csrapp.csr.ui.taketest.personalitytest

import com.csrapp.csr.data.PersonalityQuestionEntity

class PersonalityQuestionAndResponseHolder(
    val question: PersonalityQuestionEntity,
    val responseString: String? = null,
    val responseValue: Int? = null
)
