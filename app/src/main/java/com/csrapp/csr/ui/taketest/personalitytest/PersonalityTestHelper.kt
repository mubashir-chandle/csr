package com.csrapp.csr.ui.taketest.personalitytest

import com.csrapp.csr.datastructure.FuzzySet

class PersonalityTestHelper {
    companion object {
        fun generateScore(
            userTestScores: Map<Int, Double?>,
            streamRequirementFuzzySets: Map<String, FuzzySet>
        ): Map<String, Float> {

            // Make a mutable copy of stream questions.
            val requiredFuzzySets = mutableMapOf<String, FuzzySet>()
            for (key in streamRequirementFuzzySets.keys) {
                requiredFuzzySets[key] = streamRequirementFuzzySets[key]!!.copy()
            }

            val userScoreFuzzySets = mutableMapOf<String, FuzzySet>()
            val intersectedFuzzySets = mutableMapOf<String, FuzzySet>()
            val scaledFuzzySets = mutableMapOf<String, FuzzySet>()
            val result = mutableMapOf<String, Float>()

            for (stream in requiredFuzzySets.keys) {
                userScoreFuzzySets[stream] = FuzzySet()

                for (questionId in requiredFuzzySets[stream]!!.keys) {
                    val userScore = userTestScores[questionId]

                    // Remove skipped questions before calculating score.
                    if (userScore == null) {
                        requiredFuzzySets[stream]?.remove(questionId)
                    } else {
                        // Make score compatible with fuzzy sets before adding by dividing by 100.
                        userScoreFuzzySets[stream]?.add(questionId, userScore / 100)
                    }
                }

                intersectedFuzzySets[stream] =
                    requiredFuzzySets[stream]!!.intersect(userScoreFuzzySets[stream]!!)
                scaledFuzzySets[stream] =
                    intersectedFuzzySets[stream]!!.divide(requiredFuzzySets[stream]!!)

                val mean = scaledFuzzySets[stream]!!.arithmeticMean()
                when {
                    mean > 0.7 -> {
                        result[stream] = 1f
                    }
                    mean < 0.5 -> {
                        result[stream] = 0f
                    }
                    else -> {
                        result[stream] = ((0.7 - mean) / (0.7 - 0.5)).toFloat()
                    }
                }
            }

            return result
        }
    }
}