package com.csrapp.csr.ui.taketest.aptitudetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_aptitude_test.*

class AptitudeTestFragment : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: AptitudeTestViewModel
    private lateinit var questions: List<AptitudeQuestionEntity>
    private lateinit var questionResponses: MutableList<QuestionResponse>
    private var currentQuestionIndex = 0

    enum class QuestionResponse {
        ANSWERED, SKIPPED, MARKED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aptitude_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        btnNext.isEnabled = false
        btnMark.isEnabled = false

        initUI()
        assignActionListeners()
        initAptitudeTest()
    }

    private fun assignActionListeners() {
        btnNext.setOnClickListener(this)
        btnMark.setOnClickListener(this)
        btnSkip.setOnClickListener(this)

        option1.setOnCheckedChangeListener(this)
        option2.setOnCheckedChangeListener(this)
        option3.setOnCheckedChangeListener(this)
        option4.setOnCheckedChangeListener(this)
    }

    private fun initAptitudeTest() {
        questions = getRandomQuestions()
        questionResponses = mutableListOf()

        updateQuestion()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideAptitudeTestViewModelFactory(activity!!)
        viewModel = ViewModelProvider(this, factory).get(AptitudeTestViewModel::class.java)
    }

    private fun getRandomQuestions(): List<AptitudeQuestionEntity> {
        val categories = viewModel.getAptitudeCategories()

        val questions = mutableListOf<AptitudeQuestionEntity>()
        categories.forEach { category ->
            val categoryQuestions = viewModel.getAptitudeQuestionsByCategory(category)
            val selectedQuestions = categoryQuestions.shuffled().take(5)
            questions.addAll(selectedQuestions)
        }

        return questions
    }

    private fun updateQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.text
        option1.text = question.option1
        option2.text = question.option2
        option3.text = question.option3
        option4.text = question.option4
        confidence.progress = 0

        if (question.referenceImage.isNullOrBlank()) {
            referenceImage.visibility = View.GONE
        } else {
            referenceImage.visibility = View.VISIBLE
            val identifier = context!!.resources.getIdentifier(
                question.referenceImage,
                "mipmap",
                context!!.packageName
            )

            referenceImage.setImageResource(identifier)
        }

        layout.smoothScrollTo(0, 0)
        optionGroup.clearCheck()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnNext -> {
                questionResponses.add(QuestionResponse.ANSWERED)

                if (currentQuestionIndex + 2 == questions.size) {
                    Toast.makeText(context!!, "Test Completed", Toast.LENGTH_SHORT).show()
                    btnNext.isEnabled = false
                    btnMark.isEnabled = false
                    btnSkip.isEnabled = false
                }
            }

            R.id.btnMark -> {
                questionResponses.add(QuestionResponse.MARKED)
            }

            R.id.btnSkip -> {
                questionResponses.add(QuestionResponse.SKIPPED)
            }
        }

        if (v.id in listOf(
                R.id.btnNext,
                R.id.btnMark,
                R.id.btnSkip
            )
            && currentQuestionIndex != (questions.size - 1)
        ) {
            currentQuestionIndex += 1
            updateQuestion()
            btnNext.isEnabled = false
            btnMark.isEnabled = false
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView!!.id in listOf(R.id.option1, R.id.option2, R.id.option3, R.id.option4)) {
            btnNext.isEnabled = true
            btnMark.isEnabled = true
        }
    }
}
