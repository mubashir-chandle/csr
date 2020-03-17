package com.csrapp.csr.ui.taketest.aptitudetest

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.data.AptitudeQuestionRepository

class AptitudeTestViewModel(private val aptitudeQuestionRepository: AptitudeQuestionRepository) :
    ViewModel() {

    private val TEST_TIME = 1 * 60 * 1000L                // 1 Minute
    private val questions = getRandomQuestions()
    private var spinnerAdapter: SpinnerQuestionAdapter? = null

    var currentQuestionIndex = 0

    private val _timeRemaining = MutableLiveData<Long>(TEST_TIME)
    val timeRemaining: LiveData<Long>
        get() = _timeRemaining

    private val _testFinished = MutableLiveData<Boolean>(false)
    val testFinished: LiveData<Boolean>
        get() = _testFinished

    val timer = object : CountDownTimer(TEST_TIME, 1000) {
        override fun onFinish() {
            _testFinished.value = true
        }

        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished > 0) {
                _timeRemaining.value = millisUntilFinished / 1000
            }
        }
    }

    init {
        timer.start()
    }

    fun getSpinnerAdapter(context: Context): SpinnerQuestionAdapter {
        return spinnerAdapter ?: SpinnerQuestionAdapter(context, questions).also {
            this.spinnerAdapter = it
        }
    }

    fun getAptitudeQuestionsByCategory(category: String) =
        aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)

    fun getAptitudeCategories() = aptitudeQuestionRepository.getAptitudeCategories()

    private fun getRandomQuestions(): List<AptitudeQuestionEntity> {
        val categories = aptitudeQuestionRepository.getAptitudeCategories()

        val questions = mutableListOf<AptitudeQuestionEntity>()
        categories.forEach { category ->
            val categoryQuestions =
                aptitudeQuestionRepository.getAptitudeQuestionsByCategory(category)
            val selectedQuestions = categoryQuestions.shuffled().take(1)
            questions.addAll(selectedQuestions)
        }

        return questions.shuffled()
    }
}