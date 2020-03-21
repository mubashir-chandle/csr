package com.csrapp.csr.ui.taketest.personalitytest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.PersonalityQuestionEntity
import com.csrapp.csr.data.PersonalityQuestionRepository

class PersonalityTestViewModel(private val personalityQuestionRepository: PersonalityQuestionRepository) :
    ViewModel() {
    private var _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int>
        get() = _currentQuestionIndex

    private var _currentQuestionNumberDisplay = MutableLiveData<String>()
    val currentQuestionNumberDisplay: LiveData<String>
        get() = _currentQuestionNumberDisplay

    private var _testFinished = MutableLiveData(false)
    val testFinished: LiveData<Boolean>
        get() = _testFinished

    private var _btnNextText = MutableLiveData<String>("Next")
    val btnNextText: LiveData<String>
        get() = _btnNextText

    private var _sliderValueText = MutableLiveData<String>()
    val sliderValueText: LiveData<String>
        get() = _sliderValueText

    var sliderValue = MutableLiveData<Int>()

    private var _currentQuestion = MutableLiveData<PersonalityQuestionEntity>()
    val currentQuestion: LiveData<PersonalityQuestionEntity>
        get() = _currentQuestion

    var responseString = MutableLiveData<String>()

    private var _isTextualQuestion = MutableLiveData<Boolean>()
    val isTextualQuestion: LiveData<Boolean>
        get() = _isTextualQuestion

    private var questionsAndResponses: List<PersonalityQuestionAndResponseHolder>

    private var sliderValueObserver: Observer<Int>

    init {
        val questions = getRandomizedQuestions()
        val tempQuestionHolders = mutableListOf<PersonalityQuestionAndResponseHolder>()
        for (i in questions.indices) {
            tempQuestionHolders.add(PersonalityQuestionAndResponseHolder(questions[i]))
        }

        // Use sublist for easier testing.
        questionsAndResponses = tempQuestionHolders.subList(0, 10)
        _currentQuestion.value = questionsAndResponses[_currentQuestionIndex.value!!].question
        _isTextualQuestion.value = _currentQuestion.value!!.type == "textual"

        sliderValue.value = 0
        sliderValueObserver = Observer {
            val percent = sliderValue.value!! * 10
            _sliderValueText.value = "${percent}%"
        }
        sliderValue.observeForever(sliderValueObserver)
        _currentQuestionNumberDisplay.value = generateCurrentQuestionNumber()
    }

    private fun generateCurrentQuestionNumber(): String {
        val currentQuestionNumber = _currentQuestionIndex.value!! + 1
        val totalQuestions = questionsAndResponses.size

        return "Question $currentQuestionNumber/$totalQuestions"
    }

    fun onButtonNextClicked() {
        if (currentQuestion.value!!.type == "textual")
            questionsAndResponses[_currentQuestionIndex.value!!].responseString =
                responseString.value
        else
            questionsAndResponses[_currentQuestionIndex.value!!].responseValue = sliderValue.value

        if (_currentQuestionIndex.value == questionsAndResponses.lastIndex) {
            _testFinished.value = true
            return
        } else if (_currentQuestionIndex.value == questionsAndResponses.lastIndex - 1) {
            _btnNextText.value = "Finish"
        }

        _currentQuestionIndex.value = _currentQuestionIndex.value!! + 1
        _currentQuestionNumberDisplay.value = generateCurrentQuestionNumber()

        _currentQuestion.value = questionsAndResponses[_currentQuestionIndex.value!!].question
        _isTextualQuestion.value = _currentQuestion.value!!.type == "textual"

        sliderValue.value = 0
        responseString.value = ""
    }

    private fun getRandomizedQuestions(): List<PersonalityQuestionEntity> {
        return personalityQuestionRepository.getQuestions().shuffled()
    }

    fun getQuestionsAndResponses() = questionsAndResponses

    fun getTotalQuestions() = questionsAndResponses.size

    fun getStreams() = personalityQuestionRepository.getStreams()

    override fun onCleared() {
        sliderValue.removeObserver(sliderValueObserver)
        super.onCleared()
    }
}