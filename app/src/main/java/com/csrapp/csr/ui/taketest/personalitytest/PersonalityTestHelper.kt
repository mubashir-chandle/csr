package com.csrapp.csr.ui.taketest.personalitytest

import kotlin.math.roundToInt

class PersonalityTestHelper {
    companion object {
        fun generateScore(
            streams: List<String>,
            numOfQuestionsSkipped: Map<String, Int>,
            questionsAndResponses: List<StreamQuestionAndResponseHolder>,
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
                val currentScore = if (questionsAnswered == 0) {
                    // Avoid division by zero.
                    0.0
                } else {
                    (questionAndResponse.score ?: 0.0).toDouble() / questionsAnswered
                }

                val previousScore = result[question.stream]!!
                result[question.stream] = previousScore + currentScore
            }

            val roundedScore = mutableMapOf<String, Int>()
            result.forEach { (stream, score) ->
                roundedScore[stream] = score.roundToInt()
            }

            return roundedScore
        }
    }
}