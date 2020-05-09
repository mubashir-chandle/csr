package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.*
import com.csrapp.csr.R
import com.csrapp.csr.data.BasePersonalityQuestionEntity.Companion.getPersonalityQuestionType
import com.csrapp.csr.data.BasePersonalityQuestionEntity.PersonalityQuestionType.Textual
import com.csrapp.csr.data.BasePersonalityQuestionRepository
import com.csrapp.csr.data.StreamQuestionEntity
import com.csrapp.csr.data.StreamQuestionRepository
import com.csrapp.csr.data.StreamRepository
import com.csrapp.csr.nlu.NLUService
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.set

class PersonalityTestViewModel(
    private val streamRepository: StreamRepository,
    private val basePersonalityQuestionRepository: BasePersonalityQuestionRepository,
    private val streamQuestionRepository: StreamQuestionRepository
) :
    ViewModel() {

    val questionsPerStream = 1

    // Sentiment analysis questions skipped due to internet problems.
    private val sentimentalQuestionsSkipped = mutableMapOf<String, Int>()

    private var _nluErrorOccurred = MutableLiveData<NLUService.NLUError?>(null)
    val nluErrorOccurred: LiveData<NLUService.NLUError?> = _nluErrorOccurred


    private var _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    val currentQuestionNumberDisplay = currentQuestionIndex.switchMap {
        liveData { emit(currentQuestionNumber(it)) }
    }

    private var _testFinished = MutableLiveData(false)
    val testFinished: LiveData<Boolean> = _testFinished

    val btnNextText = currentQuestionIndex.switchMap {
        val text = when (it) {
            questionsAndResponses.lastIndex -> (ResourceProvider.getString(R.string.finish))
            else -> ResourceProvider.getString(R.string.next)
        }
        liveData { emit(text) }
    }

    var sliderValue = MutableLiveData<Int>()
    val sliderValueText = sliderValue.switchMap {
        liveData { emit(ResourceProvider.getString(R.string.percent, it)) }
    }

    val currentQuestion = currentQuestionIndex.switchMap {
        liveData { emit(questionsAndResponses[it].question) }
    }

    val currentBaseQuestion = currentQuestion.switchMap {
        liveData { emit(getBaseQuestionFromId(it.baseQuestionId)) }
    }

    var responseString = MutableLiveData<String>()

    val isTextualQuestion = currentBaseQuestion.switchMap {
        val textual = when (getPersonalityQuestionType(currentBaseQuestion.value!!)) {
            Textual -> true
            else -> false
        }
        liveData { emit(textual) }
    }

    private var questionsAndResponses: List<StreamQuestionAndResponseHolder>

    var loading = MutableLiveData(false)

    init {
        val questions = getRandomizedQuestions()
        val tempQuestionHolders = mutableListOf<StreamQuestionAndResponseHolder>()
        for (i in questions.indices) {
            tempQuestionHolders.add(StreamQuestionAndResponseHolder(questions[i]))
        }

        for (stream in getAllStreams()) {
            sentimentalQuestionsSkipped[stream] = 0
        }

        questionsAndResponses = tempQuestionHolders
        sliderValue.value = 0
    }


    fun skipCurrentQuestion() {
        val stream = currentQuestion.value!!.stream
        val previousSkipped = sentimentalQuestionsSkipped[stream]!!
        sentimentalQuestionsSkipped[stream] = previousSkipped + 1

        saveScoreGoToNextQuestion(null)
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

    private suspend fun performSentimentAnalysis(): Double? {
        loading.value = true

        return try {
            NLUService.performSentimentAnalysis(responseString.value!!)
        } catch (e: NLUService.NLUException) {
            _nluErrorOccurred.postValue(NLUService.NLUError.INTERNET)
            null
        } finally {
            loading.value = false
        }
    }

    fun onButtonNextClicked() {
        CoroutineScope(Dispatchers.Main).launch {
            val score: Double?

            when (getPersonalityQuestionType(currentBaseQuestion.value!!)) {
                Textual -> {
                    if (responseString.value == null || responseString.value!!.length < 5) {
                        _nluErrorOccurred.value = NLUService.NLUError.INSUFFICIENT_INPUT
                        return@launch
                    }

                    score = performSentimentAnalysis()

                    if (score == null)
                        return@launch
                }
                else -> {
                    score = sliderValue.value!!.toDouble()
                }
            }

            saveScoreGoToNextQuestion(score)
        }
    }

    private fun getRandomizedQuestions(): List<StreamQuestionEntity> {
        val questions = mutableListOf<StreamQuestionEntity>()
        getAllStreams().forEach { stream ->
            questions.addAll(
                // TODO: Use all the questions instead of just sublist.
                streamQuestionRepository.getQuestionsByStream(stream).subList(0, 1)
            )
        }
        return questions
    }

    private fun currentQuestionNumber(index: Int): String {
        val currentQuestionNumber = index + 1
        val totalQuestions = questionsAndResponses.size

        return "%02d/%02d".format(currentQuestionNumber, totalQuestions)
    }

    fun getQuestionsAndResponses() = questionsAndResponses

    fun getAllStreams(): List<String> {
        val streams = mutableListOf<String>()
        streamRepository.getAllStreams().forEach { stream ->
            streams.add(stream.id)
        }

        return streams
    }

    fun getQuestionsSkippedInEachStream() = sentimentalQuestionsSkipped

    private fun getBaseQuestionFromId(id: Int) =
        basePersonalityQuestionRepository.getQuestionById(id)
}
