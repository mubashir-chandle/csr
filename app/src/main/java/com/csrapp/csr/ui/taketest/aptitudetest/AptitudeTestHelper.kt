package com.csrapp.csr.ui.taketest.aptitudetest

import kotlin.math.roundToInt

class AptitudeTestHelper {
    companion object {
        fun generateScores(
            questionsPerCategory: Int,
            questionsAndResponseHolder: List<AptitudeQuestionAndResponseHolder>
        ): Map<String, Int> {
            val scores = mutableMapOf<String, Double>()

            for (i in questionsAndResponseHolder.indices) {
                val questionHolder = questionsAndResponseHolder[i]
                if (questionHolder.responseType == AptitudeQuestionAndResponseHolder.QuestionResponseType.UNANSWERED) {
                    if (!scores.containsKey(questionHolder.question.category))
                        scores[questionHolder.question.category] = 0.0
                    continue
                }

                val questionScore =
                    if (questionHolder.question.correctOption == questionHolder.optionSelected) {
                        // Convert to double to avoid integer division.
                        (questionHolder.confidence!!).toDouble() / questionsPerCategory
                    } else {
                        (-questionHolder.confidence!!).toDouble() / questionsPerCategory
                    }

                val previousScore = scores[questionHolder.question.category] ?: 0.0
                scores[questionHolder.question.category] = previousScore + questionScore
            }

            val roundedScores = mutableMapOf<String, Int>()
            scores.forEach { (category, score) ->
                roundedScores[category] = score.roundToInt()
            }

            return roundedScores
        }
    }
}