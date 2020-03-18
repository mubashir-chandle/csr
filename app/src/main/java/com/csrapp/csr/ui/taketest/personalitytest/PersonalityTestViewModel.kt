package com.csrapp.csr.ui.taketest.personalitytest

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

    private var _currentQuestion = MutableLiveData<PersonalityQuestionEntity>()
    val currentQuestion: LiveData<PersonalityQuestionEntity>
        get() = _currentQuestion

    private var questions: List<PersonalityQuestionEntity>

    init {
        questions = getRandomizedQuestions()
        _currentQuestion.value = questions[_currentQuestionIndex.value!!]
    }

    fun onButtonNextClicked() {
        _currentQuestionIndex.value = _currentQuestionIndex.value!! + 1
        _currentQuestion.value = questions[_currentQuestionIndex.value!!]
    }

    fun getCurrentQuestion() = questions[_currentQuestionIndex.value!!]

    private fun getRandomizedQuestions(): List<PersonalityQuestionEntity> {
        return personalityQuestionRepository.getQuestions().shuffled()
    }
}