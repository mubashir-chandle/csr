package com.csrapp.csr.nlu

import androidx.lifecycle.MutableLiveData
import com.csrapp.csr.R
import com.csrapp.csr.utils.ResourceProvider
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions
import com.ibm.watson.natural_language_understanding.v1.model.EmotionOptions
import com.ibm.watson.natural_language_understanding.v1.model.Features
import com.ibm.watson.natural_language_understanding.v1.model.SentimentOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object NLUService {
    class NLUException(message: String? = null) : Exception(message)

    enum class NLUError {
        INTERNET, BAD_RESPONSE, INSUFFICIENT_INPUT
    }

    private val nluService by lazy {
        val authenticator = IamAuthenticator(ResourceProvider.getString(R.string.nlu_apikey))
        val service = NaturalLanguageUnderstanding(
            ResourceProvider.getString(R.string.nlu_version_date),
            authenticator
        )
        service.serviceUrl = ResourceProvider.getString(R.string.nlu_url)
        service
    }

    private val nluFeatures by lazy {
        val sentimentOptions = SentimentOptions.Builder().document(true).build()
        val emotionOptions = EmotionOptions.Builder().document(true).build()

        Features.Builder()
            .sentiment(sentimentOptions)
            .emotion(emotionOptions)
            .build()
    }

    suspend fun performSentimentAnalysis(string: String): Double? {
        val parameters = AnalyzeOptions.Builder()
            .text(string)
            .features(nluFeatures)
            .language("en")
            .build()

        val score = MutableLiveData<Double>()
        withContext(Dispatchers.IO) {
            val results = try {
                nluService.analyze(parameters).execute().result
            } catch (e: Exception) {
                throw NLUException()
            }

            val serviceScore = results.sentiment?.document?.score!!
            score.postValue(((serviceScore + 1) / 2) * 100)
        }

        while (score.value == null) {
            delay(100)
        }

        return score.value
    }
}