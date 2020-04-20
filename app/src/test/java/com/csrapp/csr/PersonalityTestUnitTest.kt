package com.csrapp.csr

import com.csrapp.csr.data.PersonalityQuestionEntity
import com.csrapp.csr.ui.taketest.personalitytest.PersonalityQuestionAndResponseHolder
import com.csrapp.csr.ui.taketest.personalitytest.PersonalityTestHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonalityTestUnitTest {
    companion object {
        private val streams = listOf("stream 1", "stream 2", "stream 3", "stream 4")

        private val fakeQuestions by lazy {
            val q = mutableListOf<PersonalityQuestionEntity>()
            with(q) {
                add(PersonalityQuestionEntity(1, "stream 1", "", "slider"))
                add(PersonalityQuestionEntity(2, "stream 1", "", "slider"))
                add(PersonalityQuestionEntity(3, "stream 2", "", "slider"))
                add(PersonalityQuestionEntity(4, "stream 2", "", "slider"))
                add(PersonalityQuestionEntity(5, "stream 3", "", "slider"))
                add(PersonalityQuestionEntity(6, "stream 3", "", "slider"))
                add(PersonalityQuestionEntity(7, "stream 4", "", "slider"))
                add(PersonalityQuestionEntity(8, "stream 4", "", "slider"))
            }
            q
        }
    }

    @Test
    fun generateScore_all50() {
        val numOfQuestionsSkipped = mutableMapOf<String, Int>()
        streams.forEach { stream ->
            numOfQuestionsSkipped[stream] = 0
        }

        val fakeResponses = mutableListOf<PersonalityQuestionAndResponseHolder>()
        with(fakeResponses) {
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[0], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[1], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[2], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[3], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[4], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[5], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[6], 50.0))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[7], 50.0))
        }

        val result =
            PersonalityTestHelper.generateScore(streams, numOfQuestionsSkipped, fakeResponses, 2)

        assertEquals(result["stream 1"], 50)
        assertEquals(result["stream 2"], 50)
        assertEquals(result["stream 3"], 50)
        assertEquals(result["stream 4"], 50)
    }

    @Test
    fun generateScore_allSkipped() {
        val numOfQuestionsSkipped = mutableMapOf<String, Int>()
        streams.forEach { stream ->
            numOfQuestionsSkipped[stream] = 2
        }

        val fakeResponses = mutableListOf<PersonalityQuestionAndResponseHolder>()
        with(fakeResponses) {
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[0], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[1], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[2], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[3], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[4], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[5], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[6], null))
            add(PersonalityQuestionAndResponseHolder(fakeQuestions[7], null))
        }

        val result =
            PersonalityTestHelper.generateScore(streams, numOfQuestionsSkipped, fakeResponses, 2)

        assertEquals(result["stream 1"], 0)
        assertEquals(result["stream 2"], 0)
        assertEquals(result["stream 3"], 0)
        assertEquals(result["stream 4"], 0)
    }
}