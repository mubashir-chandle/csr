package com.csrapp.csr

import com.csrapp.csr.data.StreamQuestionEntity.Companion.getNumericalImportanceValue
import com.csrapp.csr.data.StreamQuestionEntity.Importance
import com.csrapp.csr.datastructure.FuzzySet
import com.csrapp.csr.ui.taketest.personalitytest.PersonalityTestHelper
import com.csrapp.csr.ui.taketest.result.RecommendationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonalityTestUnitTest {
    companion object {
        private val streamQuestions by lazy {
            val q = mutableMapOf<String, FuzzySet>()

            q["stream 1"] = FuzzySet()
            q["stream 2"] = FuzzySet()
            q["stream 3"] = FuzzySet()
            q["stream 4"] = FuzzySet()

            q["stream 1"]?.add(1, getNumericalImportanceValue(Importance.Low))
            q["stream 2"]?.add(2, getNumericalImportanceValue(Importance.Medium))
            q["stream 3"]?.add(3, getNumericalImportanceValue(Importance.High))
            q["stream 4"]?.add(4, getNumericalImportanceValue(Importance.Low))

            // Add an extra question which is also used for some other stream.
            q["stream 4"]?.add(3, getNumericalImportanceValue(Importance.Medium))

            q
        }
    }

    @Test
    fun generateScore_mixed() {
        val userScores = mutableMapOf<Int, Double>()
        userScores[1] = getNumericalImportanceValue(Importance.Low) / 2
        userScores[2] = getNumericalImportanceValue(Importance.Medium)
        userScores[3] = getNumericalImportanceValue(Importance.High) / 2
        userScores[4] = getNumericalImportanceValue(Importance.Low) / 2

        val result = PersonalityTestHelper.generateScore(userScores, streamQuestions)

        assertEquals(result["stream 1"], RecommendationResult.No)
        assertEquals(result["stream 2"], RecommendationResult.Yes)
        assertEquals(result["stream 3"], RecommendationResult.No)
        assertEquals(result["stream 4"], RecommendationResult.Maybe)
    }

    @Test
    fun generateScore_allSkipped() {
        val userScores = mutableMapOf<Int, Double>()
        userScores[1] = 0.0
        userScores[2] = 0.0
        userScores[3] = 0.0
        userScores[4] = 0.0

        val result = PersonalityTestHelper.generateScore(userScores, streamQuestions)

        assertEquals(result["stream 1"], RecommendationResult.No)
        assertEquals(result["stream 2"], RecommendationResult.No)
        assertEquals(result["stream 3"], RecommendationResult.No)
        assertEquals(result["stream 4"], RecommendationResult.No)
    }

    @Test
    fun generateScore_all100() {
        val userScores = mutableMapOf<Int, Double>()
        userScores[1] = 1.0
        userScores[2] = 1.0
        userScores[3] = 1.0
        userScores[4] = 1.0

        val result = PersonalityTestHelper.generateScore(userScores, streamQuestions)

        assertEquals(result["stream 1"], RecommendationResult.Yes)
        assertEquals(result["stream 2"], RecommendationResult.Yes)
        assertEquals(result["stream 3"], RecommendationResult.Yes)
        assertEquals(result["stream 4"], RecommendationResult.Yes)
    }
}
