package com.csrapp.csr

import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.ui.taketest.aptitudetest.AptitudeQuestionAndResponseHolder
import com.csrapp.csr.ui.taketest.aptitudetest.AptitudeQuestionAndResponseHolder.QuestionResponseType.*
import com.csrapp.csr.ui.taketest.aptitudetest.AptitudeTestHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class AptitudeTestUnitTest {
    companion object {
        private val fakeQuestions by lazy {
            val questions = mutableListOf<AptitudeQuestionEntity>()
            questions.add(AptitudeQuestionEntity(1, "logical", "", "", "", "", "", "", 1, ""))
            questions.add(AptitudeQuestionEntity(2, "logical", "", "", "", "", "", "", 1, ""))
            questions.add(AptitudeQuestionEntity(3, "numerical", "", "", "", "", "", "", 2, ""))
            questions.add(AptitudeQuestionEntity(4, "numerical", "", "", "", "", "", "", 2, ""))
            questions.add(AptitudeQuestionEntity(5, "spatial", "", "", "", "", "", "", 3, ""))
            questions.add(AptitudeQuestionEntity(6, "spatial", "", "", "", "", "", "", 3, ""))
            questions.add(AptitudeQuestionEntity(7, "verbal", "", "", "", "", "", "", 4, ""))
            questions.add(AptitudeQuestionEntity(8, "verbal", "", "", "", "", "", "", 4, ""))
            questions
        }
    }

    @Test
    fun generateScore_allCorrect() {
        val questionsAndResponsesHolder = mutableListOf<AptitudeQuestionAndResponseHolder>()
        with(questionsAndResponsesHolder) {
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[0], ANSWERED, 1, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[1], ANSWERED, 1, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[2], ANSWERED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[3], ANSWERED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[4], ANSWERED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[5], ANSWERED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[6], ANSWERED, 4, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[7], ANSWERED, 4, 50))
        }

        val scores = AptitudeTestHelper.generateScores(2, questionsAndResponsesHolder)
        assertEquals(scores["logical"], 50)
        assertEquals(scores["numerical"], 50)
        assertEquals(scores["spatial"], 50)
        assertEquals(scores["verbal"], 50)
    }

    @Test
    fun generateScore_allWrong() {
        val questionsAndResponsesHolder = mutableListOf<AptitudeQuestionAndResponseHolder>()
        with(questionsAndResponsesHolder) {
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[0], ANSWERED, 4, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[1], ANSWERED, 4, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[2], ANSWERED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[3], ANSWERED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[4], ANSWERED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[5], ANSWERED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[6], ANSWERED, 1, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[7], ANSWERED, 1, 50))
        }

        val scores = AptitudeTestHelper.generateScores(2, questionsAndResponsesHolder)
        assertEquals(scores["logical"], -50)
        assertEquals(scores["numerical"], -50)
        assertEquals(scores["spatial"], -50)
        assertEquals(scores["verbal"], -50)
    }

    @Test
    fun generateScore_allMarked() {
        val questionsAndResponsesHolder = mutableListOf<AptitudeQuestionAndResponseHolder>()
        with(questionsAndResponsesHolder) {
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[0], MARKED, 1, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[1], MARKED, 1, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[2], MARKED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[3], MARKED, 2, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[4], MARKED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[5], MARKED, 3, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[6], MARKED, 4, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[7], MARKED, 4, 50))
        }

        val scores = AptitudeTestHelper.generateScores(2, questionsAndResponsesHolder)
        assertEquals(scores["logical"], 50)
        assertEquals(scores["numerical"], 50)
        assertEquals(scores["spatial"], 50)
        assertEquals(scores["verbal"], 50)
    }

    @Test
    fun generateScore_allSkipped() {
        val questionsAndResponsesHolder = mutableListOf<AptitudeQuestionAndResponseHolder>()
        with(questionsAndResponsesHolder) {
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[0], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[1], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[2], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[3], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[4], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[5], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[6], UNANSWERED, null, 50))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[7], UNANSWERED, null, 50))
        }

        val scores = AptitudeTestHelper.generateScores(2, questionsAndResponsesHolder)
        assertEquals(scores["logical"], 0)
        assertEquals(scores["numerical"], 0)
        assertEquals(scores["spatial"], 0)
        assertEquals(scores["verbal"], 0)
    }

    @Test
    fun generateScore_mixed() {
        val questionsAndResponsesHolder = mutableListOf<AptitudeQuestionAndResponseHolder>()
        with(questionsAndResponsesHolder) {
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[0], ANSWERED, 4, 59))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[1], UNANSWERED, null, 0))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[2], MARKED, 2, 30))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[3], ANSWERED, 2, 100))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[4], UNANSWERED, null, 0))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[5], UNANSWERED, null, 0))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[6], MARKED, 3, 78))
            add(AptitudeQuestionAndResponseHolder(fakeQuestions[7], MARKED, 4, 33))
        }

        val scores = AptitudeTestHelper.generateScores(2, questionsAndResponsesHolder)
        assertEquals(scores["logical"], -29)
        assertEquals(scores["numerical"], 65)
        assertEquals(scores["spatial"], 0)
        assertEquals(scores["verbal"], -22)
    }
}
