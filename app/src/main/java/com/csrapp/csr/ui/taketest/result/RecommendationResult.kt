package com.csrapp.csr.ui.taketest.result

enum class RecommendationResult {
    No, Yes, Maybe;

    companion object {
        fun intToRecommendationResult(value: Int): RecommendationResult = when (value) {
            0 -> No
            1 -> Yes
            2 -> Maybe
            else -> throw IllegalArgumentException("Recommendation result value must be either 0, 1 or 2.")
        }

        fun recommendationResultToInt(result: RecommendationResult) = when (result) {
            No -> 0
            Yes -> 1
            Maybe -> 2
        }
    }
}