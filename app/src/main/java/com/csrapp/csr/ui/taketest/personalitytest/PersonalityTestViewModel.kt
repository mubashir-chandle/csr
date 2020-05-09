package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.*
import com.csrapp.csr.R
import com.csrapp.csr.data.BasePersonalityQuestionEntity.Companion.getPersonalityQuestionType
import com.csrapp.csr.data.BasePersonalityQuestionEntity.PersonalityQuestionType.Textual
import com.csrapp.csr.data.BasePersonalityQuestionRepository
import com.csrapp.csr.data.StreamQuestionRepository
import com.csrapp.csr.data.StreamRepository
import com.csrapp.csr.datastructure.FuzzySet
import com.csrapp.csr.nlu.NLUService
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonalityTestViewModel(
    private val streamRepository: StreamRepository,
    private val basePersonalityQuestionRepository: BasePersonalityQuestionRepository,
    private val streamQuestionRepository: StreamQuestionRepository
) :
    ViewModel() {

    private var _nluErrorOccurred = MutableLiveData<NLUService.NLUError?>(null)
    val nluErrorOccurred: LiveData<NLUService.NLUError?> = _nluErrorOccurred

    private var questionNumber = MutableLiveData(0)

    private var questions = basePersonalityQuestionRepository.getAllQuestions()

    val currentQuestionNumberDisplay = questionNumber.switchMap {
        liveData { emit(currentQuestionNumber(it)) }
    }

    private var _testFinished = MutableLiveData(false)
    val testFinished: LiveData<Boolean> = _testFinished

    val btnNextText = questionNumber.switchMap {
        val text = when (it) {
            userScore.size - 1 -> (ResourceProvider.getString(R.string.finish))
            else -> ResourceProvider.getString(R.string.next)
        }
        liveData { emit(text) }
    }

    var sliderValue = MutableLiveData<Int>()
    val sliderValueText = sliderValue.switchMap {
        liveData { emit(ResourceProvider.getString(R.string.percent, it)) }
    }

    val currentQuestion = questionNumber.switchMap {
        liveData { emit(questions[it]) }
    }

    var responseString = MutableLiveData<String>()

    val isTextualQuestion = currentQuestion.switchMap {
        val textual = when (getPersonalityQuestionType(currentQuestion.value!!)) {
            Textual -> true
            else -> false
        }
        liveData { emit(textual) }
    }

    private var userScore = mutableMapOf<Int, Double?>()

    var loading = MutableLiveData(false)

    init {
        sliderValue.value = 0
    }

    fun skipCurrentQuestion() {
        saveScoreGoToNextQuestion(null)
    }

    private fun saveScoreGoToNextQuestion(score: Double?) {
        userScore[currentQuestion.value!!.id!!] = score

        if (questionNumber.value == questions.size - 1) {
            _testFinished.value = true
        } else {
            questionNumber.value = questionNumber.value!! + 1
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

    private fun currentQuestionNumber(index: Int): String {
        return "%02d/%02d".format(index + 1, questions.size)
    }

    // Make only immutable copy of scores available to other classes.
    fun getUserScores(): Map<Int, Double?> = userScore

    fun getAllStreams(): List<String> {
        val streams = mutableListOf<String>()
        streamRepository.getAllStreams().forEach { stream ->
            streams.add(stream.id)
        }

        return streams
    }

    fun getStreamQuestions(): Map<String, FuzzySet> {
        val questions = mutableMapOf<String, FuzzySet>()
        for (stream in getAllStreams()) {
            questions[stream] = FuzzySet()
            for (question in streamQuestionRepository.getQuestionsByStream(stream)) {
                questions[stream]?.add(
                    question.baseQuestionId,
                    question.getNumericalImportanceValue()
                )
            }
        }

        return questions
    }
}
