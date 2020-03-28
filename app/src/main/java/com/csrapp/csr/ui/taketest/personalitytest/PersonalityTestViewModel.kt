package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.csrapp.csr.R
import com.csrapp.csr.data.PersonalityQuestionEntity
import com.csrapp.csr.data.PersonalityQuestionEntity.Companion.getPersonalityQuestionType
import com.csrapp.csr.data.PersonalityQuestionEntity.PersonalityQuestionType.Textual
import com.csrapp.csr.data.PersonalityQuestionRepository
import com.csrapp.csr.utils.ResourceProvider
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.cloud.sdk.core.service.exception.BadRequestException
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions
import com.ibm.watson.natural_language_understanding.v1.model.EmotionOptions
import com.ibm.watson.natural_language_understanding.v1.model.Features
import com.ibm.watson.natural_language_understanding.v1.model.SentimentOptions
import kotlinx.coroutines.*
import kotlin.collections.set

class PersonalityTestViewModel(private val personalityQuestionRepository: PersonalityQuestionRepository) :
    ViewModel() {

    val questionsPerStream = 1

    // Sentiment analysis questions skipped due to internet problems.
    private val sentimentalQuestionsSkipped = mutableMapOf<String, Int>()

    private var _nluErrorOccurred = MutableLiveData<NLUError?>(null)
    val nluErrorOccurred: LiveData<NLUError?>
        get() = _nluErrorOccurred

    enum class NLUError {
        INTERNET, BAD_RESPONSE, INSUFFICIENT_INPUT
    }

    private var _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int>
        get() = _currentQuestionIndex

    val currentQuestionNumberDisplay: LiveData<String> =
        Transformations.switchMap(currentQuestionIndex) { index ->
            MutableLiveData(currentQuestionNumber(index))
        }

    private var _testFinished = MutableLiveData(false)
    val testFinished: LiveData<Boolean>
        get() = _testFinished

    val btnNextText: LiveData<String> = Transformations.switchMap(currentQuestionIndex) { index ->
        val text = when (index) {
            questionsAndResponses.lastIndex -> (ResourceProvider.getString(R.string.finish))
            else -> ResourceProvider.getString(R.string.next)
        }
        MutableLiveData(text)
    }

    var sliderValue = MutableLiveData<Int>()
    val sliderValueText: LiveData<String> = Transformations.switchMap(sliderValue) {
        MutableLiveData(ResourceProvider.getString(R.string.percent, it))
    }

    val currentQuestion: LiveData<PersonalityQuestionEntity> = Transformations
        .switchMap(currentQuestionIndex) { index ->
            MutableLiveData(questionsAndResponses[index].question)
        }

    var responseString = MutableLiveData<String>()

    val isTextualQuestion: LiveData<Boolean> = Transformations
        .switchMap(currentQuestion) {
            val textual = when (getPersonalityQuestionType(it)) {
                Textual -> true
                else -> false
            }
            MutableLiveData(textual)
        }

    private var questionsAndResponses: List<PersonalityQuestionAndResponseHolder>

    var loading = MutableLiveData(false)

    private var nluService: NaturalLanguageUnderstanding
    private var nluFeatures: Features

    init {
        val questions = getRandomizedQuestions()
        val tempQuestionHolders = mutableListOf<PersonalityQuestionAndResponseHolder>()
        for (i in questions.indices) {
            tempQuestionHolders.add(PersonalityQuestionAndResponseHolder(questions[i]))
        }

        for (stream in getStreams()) {
            sentimentalQuestionsSkipped[stream] = 0
        }

        nluFeatures = initNLUFeatures()
        nluService = initNLUService()

        questionsAndResponses = tempQuestionHolders
        sliderValue.value = 0
    }

    private fun initNLUService(): NaturalLanguageUnderstanding {
        val authenticator = IamAuthenticator(ResourceProvider.getString(R.string.nlu_apikey))
        nluService = NaturalLanguageUnderstanding(
            ResourceProvider.getString(R.string.nlu_version_date),
            authenticator
        )
        nluService.serviceUrl = ResourceProvider.getString(R.string.nlu_url)

        return nluService
    }

    private fun initNLUFeatures(): Features {
        val sentimentOptions = SentimentOptions.Builder().document(true).build()
        val emotionOptions = EmotionOptions.Builder().document(true).build()

        return Features.Builder()
            .sentiment(sentimentOptions)
            .emotion(emotionOptions)
            .build()
    }

    private fun currentQuestionNumber(index: Int): String {
        val currentQuestionNumber = index + 1
        val totalQuestions = questionsAndResponses.size

        return "%02d/%02d".format(currentQuestionNumber, totalQuestions)
    }

    suspend fun performSentimentAnalysis(string: String): Double? {
        val parameters = AnalyzeOptions.Builder()
            .text(string)
            .features(nluFeatures)
            .language("en")
            .build()

        withContext(Dispatchers.Main) {
            loading.value = true
        }

        var score: Double? = null

        try {
            val results = nluService.analyze(parameters).execute().result
            score = results.sentiment?.document?.score
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                when (e) {
                    is BadRequestException, is ServiceResponseException -> {
                        _nluErrorOccurred.value = NLUError.BAD_RESPONSE
                    }
                    else -> {
                        _nluErrorOccurred.value = NLUError.INTERNET
                    }
                }
            }

        } finally {
            withContext(Dispatchers.Main) {
                loading.value = false
            }
        }

        return if (score == null) {
            null
        } else {
            ((score + 1) / 2) * 100
        }
    }

    fun skipCurrentQuestion() {
        val stream = currentQuestion.value!!.stream!!
        val previousSkipped = sentimentalQuestionsSkipped[stream]!!
        sentimentalQuestionsSkipped[stream] = previousSkipped + 1

        saveScoreGoToNextQuestion(0.0)
    }

    private fun saveScoreGoToNextQuestion(score: Double?) {

        questionsAndResponses[_currentQuestionIndex.value!!].score = score

        if (_currentQuestionIndex.value == questionsAndResponses.lastIndex) {
            _testFinished.value = true
        } else {
            _currentQuestionIndex.value = _currentQuestionIndex.value!! + 1
            sliderValue.value = 0
            responseString.value = ""
        }
    }

    fun onButtonNextClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            val score: Double?

            when (getPersonalityQuestionType(currentQuestion.value!!)) {
                Textual -> {
                    if (responseString.value!!.length < 5) {
                        withContext(Dispatchers.Main) {
                            _nluErrorOccurred.value = NLUError.INSUFFICIENT_INPUT
                        }
                        cancel()
                    }

                    score = performSentimentAnalysis(responseString.value!!)
                    if (score == null)
                        cancel()
                }
                else -> {
                    score = sliderValue.value!!.toDouble()
                }
            }

            withContext(Dispatchers.Main) {
                saveScoreGoToNextQuestion(score)
            }
        }
    }

    private fun getRandomizedQuestions(): List<PersonalityQuestionEntity> {
        val questions = mutableListOf<PersonalityQuestionEntity>()
        val streams = personalityQuestionRepository.getStreams()
        streams.forEach { stream ->
            questions.addAll(
                personalityQuestionRepository.getQuestionsByStream(
                    stream,
                    questionsPerStream
                )
            )
        }
        return questions
    }

    fun getQuestionsAndResponses() = questionsAndResponses

    fun getStreams() = personalityQuestionRepository.getStreams()

    fun getQuestionsSkippedInEachStream() = sentimentalQuestionsSkipped
}
