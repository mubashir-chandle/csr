package com.csrapp.csr

import com.csrapp.csr.data.BasePersonalityQuestionEntity
import com.csrapp.csr.ui.taketest.personalitytest.PersonalityTestHelper
import com.csrapp.csr.ui.taketest.personalitytest.StreamQuestionAndResponseHolder
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonalityTestUnitTest {
    companion object {
        private val streams = listOf("stream 1", "stream 2", "stream 3", "stream 4")

        private val fakeQuestions by lazy {
            val q = mutableListOf<BasePersonalityQuestionEntity>()
            with(q) {
                add(BasePersonalityQuestionEntity(1, "stream 1", "", "slider"))
                add(BasePersonalityQuestionEntity(2, "stream 1", "", "slider"))
                add(BasePersonalityQuestionEntity(3, "stream 2", "", "slider"))
                add(BasePersonalityQuestionEntity(4, "stream 2", "", "slider"))
                add(BasePersonalityQuestionEntity(5, "stream 3", "", "slider"))
                add(BasePersonalityQuestionEntity(6, "stream 3", "", "slider"))
                add(BasePersonalityQuestionEntity(7, "stream 4", "", "slider"))
                add(BasePersonalityQuestionEntity(8, "stream 4", "", "slider"))
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

        val fakeResponses = mutableListOf<StreamQuestionAndResponseHolder>()
        with(fakeResponses) {
            add(StreamQuestionAndResponseHolder(fakeQuestions[0], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[1], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[2], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[3], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[4], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[5], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[6], 50.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[7], 50.0))
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

        val fakeResponses = mutableListOf<StreamQuestionAndResponseHolder>()
        with(fakeResponses) {
            add(StreamQuestionAndResponseHolder(fakeQuestions[0], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[1], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[2], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[3], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[4], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[5], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[6], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[7], null))
        }

        val result =
            PersonalityTestHelper.generateScore(streams, numOfQuestionsSkipped, fakeResponses, 2)

        assertEquals(result["stream 1"], 0)
        assertEquals(result["stream 2"], 0)
        assertEquals(result["stream 3"], 0)
        assertEquals(result["stream 4"], 0)
    }

    @Test
    fun generateScore_mixed() {
        val numOfQuestionsSkipped = mutableMapOf<String, Int>()
        numOfQuestionsSkipped["stream 1"] = 1
        numOfQuestionsSkipped["stream 2"] = 0
        numOfQuestionsSkipped["stream 3"] = 2
        numOfQuestionsSkipped["stream 4"] = 0

        val fakeResponses = mutableListOf<StreamQuestionAndResponseHolder>()
        with(fakeResponses) {
            add(StreamQuestionAndResponseHolder(fakeQuestions[0], 44.5))
            add(StreamQuestionAndResponseHolder(fakeQuestions[1], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[2], 25.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[3], 45.0))
            add(StreamQuestionAndResponseHolder(fakeQuestions[4], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[5], null))
            add(StreamQuestionAndResponseHolder(fakeQuestions[6], 99.5))
            add(StreamQuestionAndResponseHolder(fakeQuestions[7], 99.5))
        }

        val result =
            PersonalityTestHelper.generateScore(streams, numOfQuestionsSkipped, fakeResponses, 2)

        assertEquals(result["stream 1"], 45)
        assertEquals(result["stream 2"], 35)
        assertEquals(result["stream 3"], 0)
        assertEquals(result["stream 4"], 100)
    }
}
