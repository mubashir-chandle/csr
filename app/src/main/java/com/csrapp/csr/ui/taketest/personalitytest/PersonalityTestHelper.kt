package com.csrapp.csr.ui.taketest.personalitytest

import kotlin.math.roundToInt

class PersonalityTestHelper {
    companion object {
        fun generateScore(
            streams: List<String>,
            numOfQuestionsSkipped: Map<String, Int>,
            questionsAndResponses: List<PersonalityQuestionAndResponseHolder>,
            questionsPerStream: Int
        ): Map<String, Int> {

            val result = mutableMapOf<String, Double>()
            streams.forEach { stream ->
                result[stream] = 0.0
            }

            questionsAndResponses.forEach { questionAndResponse ->
                val question = questionAndResponse.question
                val questionsAnswered =
                    questionsPerStream - numOfQuestionsSkipped[question.stream]!!
                var currentScore = (questionAndResponse.score ?: 0.0).toDouble() / questionsAnswered

                // Score can be NaN if all the questions of a stream are skipped.
                if (currentScore.isNaN())
                    currentScore = 0.0

                val previousScore = result[question.stream]!!
                result[question.stream!!] = previousScore + currentScore
            }

            val roundedScore = mutableMapOf<String, Int>()
            result.forEach { (stream, score) ->
                roundedScore[stream] = score.roundToInt()
            }

            return roundedScore
        }
    }
}