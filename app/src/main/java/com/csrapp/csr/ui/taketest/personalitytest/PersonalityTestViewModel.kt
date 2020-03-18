package com.csrapp.csr.ui.taketest.personalitytest

import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.PersonalityQuestionEntity
import com.csrapp.csr.data.PersonalityQuestionRepository

class PersonalityTestViewModel(private val personalityQuestionRepository: PersonalityQuestionRepository) :
    ViewModel() {
    private var _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int>
        get() = _currentQuestionIndex

    private var _sliderValue = MutableLiveData<String>()
    val sliderValue: LiveData<String>
        get() = _sliderValue

    fun onSliderValueChanged(seekBar: SeekBar, progressValue: Int, fromUser: Boolean) {
        val percent = (progressValue + 1) * 10
        _sliderValue.value = "${percent}%"
    }

    private var _currentQuestion = MutableLiveData<PersonalityQuestionEntity>()
    val currentQuestion: LiveData<PersonalityQuestionEntity>
        get() = _currentQuestion

    private var _isTextualQuestion = MutableLiveData<Boolean>()
    val isTextualQuestion: LiveData<Boolean>
        get() = _isTextualQuestion

    private var questionsAndResponses: List<PersonalityQuestionAndResponseHolder>

    init {
        val questions = getRandomizedQuestions()
        val tempQuestionHolders = mutableListOf<PersonalityQuestionAndResponseHolder>()
        for (i in questions.indices) {
            tempQuestionHolders.add(PersonalityQuestionAndResponseHolder(questions[i]))
        }

        questionsAndResponses = tempQuestionHolders
        _currentQuestion.value = questionsAndResponses[_currentQuestionIndex.value!!].question
        _isTextualQuestion.value = _currentQuestion.value!!.type == "textual"
    }

    fun onButtonNextClicked() {
        _currentQuestionIndex.value = _currentQuestionIndex.value!! + 1
        _currentQuestion.value = questionsAndResponses[_currentQuestionIndex.value!!].question
        _isTextualQuestion.value = _currentQuestion.value!!.type == "textual"
    }

    private fun getRandomizedQuestions(): List<PersonalityQuestionEntity> {
        return personalityQuestionRepository.getQuestions().shuffled()
    }
}