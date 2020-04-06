package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.*
import com.csrapp.csr.R
import com.csrapp.csr.data.PersonalityQuestionEntity
import com.csrapp.csr.data.PersonalityQuestionEntity.Companion.getPersonalityQuestionType
import com.csrapp.csr.data.PersonalityQuestionEntity.PersonalityQuestionType.Textual
import com.csrapp.csr.data.PersonalityQuestionRepository
import com.csrapp.csr.nlu.NLUService
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.set

class PersonalityTestViewModel(private val personalityQuestionRepository: PersonalityQuestionRepository) :
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

    var responseString = MutableLiveData<String>()

    val isTextualQuestion = currentQuestion.switchMap {
        val textual = when (getPersonalityQuestionType(it)) {
            Textual -> true
            else -> false
        }
        liveData { emit(textual) }
    }

    private var questionsAndResponses: List<PersonalityQuestionAndResponseHolder>

    var loading = MutableLiveData(false)

    init {
        val questions = getRandomizedQuestions()
        val tempQuestionHolders = mutableListOf<PersonalityQuestionAndResponseHolder>()
        for (i in questions.indices) {
            tempQuestionHolders.add(PersonalityQuestionAndResponseHolder(questions[i]))
        }

        for (stream in getStreams()) {
            sentimentalQuestionsSkipped[stream] = 0
        }

        questionsAndResponses = tempQuestionHolders
        sliderValue.value = 0
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

            when (getPersonalityQuestionType(currentQuestion.value!!)) {
                Textual -> {
                    if (responseString.value!!.length < 5) {
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

    private fun currentQuestionNumber(index: Int): String {
        val currentQuestionNumber = index + 1
        val totalQuestions = questionsAndResponses.size

        return "%02d/%02d".format(currentQuestionNumber, totalQuestions)
    }

    fun getQuestionsAndResponses() = questionsAndResponses

    fun getStreams() = personalityQuestionRepository.getStreams()

    fun getQuestionsSkippedInEachStream() = sentimentalQuestionsSkipped
}
