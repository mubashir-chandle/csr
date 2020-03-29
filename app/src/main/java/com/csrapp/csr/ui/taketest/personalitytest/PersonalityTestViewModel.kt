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
    val nluErrorOccurred: LiveData<NLUService.NLUError?>
        get() = _nluErrorOccurred


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
