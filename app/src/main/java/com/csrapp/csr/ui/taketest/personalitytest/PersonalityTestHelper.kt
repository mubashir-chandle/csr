package com.csrapp.csr.ui.taketest.personalitytest

import com.csrapp.csr.datastructure.FuzzySet
import kotlin.math.roundToInt

class PersonalityTestHelper {
    companion object {
        fun generateScore(
            streams: List<String>,
            questionsAndResponses: List<StreamQuestionAndResponseHolder>
        ): Map<String, Int> {

            val resultFuzzySet = mutableMapOf<String, FuzzySet>()
            val responseFuzzySet = mutableMapOf<String, FuzzySet>()
            val intersectedFuzzySet = mutableMapOf<String, FuzzySet>()
            val scaledFuzzySet = mutableMapOf<String, FuzzySet>()

            streams.forEach { stream ->
                resultFuzzySet[stream] = FuzzySet()
                responseFuzzySet[stream] = FuzzySet()
                intersectedFuzzySet[stream] = FuzzySet()
                scaledFuzzySet[stream] = FuzzySet()
            }

            for (questionAndResponse in questionsAndResponses) {
                val stream = questionAndResponse.question.stream
                val baseQuestionId = questionAndResponse.question.baseQuestionId
                val importance = questionAndResponse.question.getNumericalImportanceValue()
                val userPreference = questionAndResponse.score

                // Ignore the skipped questions from score generation.
                if (userPreference != null) {
                    resultFuzzySet[stream]?.add(baseQuestionId, importance)
                    responseFuzzySet[stream]?.add(baseQuestionId, userPreference / 100)
                }
            }

            // Find score using Fuzzy set intersection, division (scaling) and arithmetic mean.
            val result = mutableMapOf<String, Double>()
            for (key in intersectedFuzzySet.keys) {
                if (key !in resultFuzzySet || key !in responseFuzzySet) {
                    throw Exception("Keys in all the fuzzy sets must be same.")
                } else if (resultFuzzySet[key] == null || responseFuzzySet[key] == null) {
                    throw Exception("Fuzzy set for {key} not initialized")
                }

                intersectedFuzzySet[key] = resultFuzzySet[key]!!.intersect(responseFuzzySet[key]!!)
                scaledFuzzySet[key] = intersectedFuzzySet[key]!!.divide(resultFuzzySet[key]!!)

                // TODO: Remove the multiplication by 100.
                result[key] = scaledFuzzySet[key]!!.arithmeticMean() * 100
            }

            // TODO: Remove score rounding.
            val roundedScore = mutableMapOf<String, Int>()
            result.forEach { (stream, score) ->
                roundedScore[stream] = score.roundToInt()
            }

            return roundedScore

//            questionsAndResponses.forEach { questionAndResponse ->
//                val question = questionAndResponse.question
//                val questionsAnswered =
//                    questionsPerStream - numOfQuestionsSkipped[question.stream]!!
//                val currentScore = if (questionsAnswered == 0) {
//                    // Avoid division by zero.
//                    0.0
//                } else {
//                    (questionAndResponse.score ?: 0.0).toDouble() / questionsAnswered
//                }
//
//                val previousScore = result[question.stream]!!
//                result[question.stream] = previousScore + currentScore
//            }
//
//            val roundedScore = mutableMapOf<String, Int>()
//            result.forEach { (stream, score) ->
//                roundedScore[stream] = score.roundToInt()
//            }
//
//            return roundedScore
        }
    }
}