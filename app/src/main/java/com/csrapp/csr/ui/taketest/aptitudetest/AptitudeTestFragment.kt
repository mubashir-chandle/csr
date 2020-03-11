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
    private lateinit var questionResponses: Array<QuestionResponse?>
    private var currentQuestionIndex = 0


    class QuestionResponse(
        var responseType: QuestionResponseType,
        var optionSelected: Int? = null,
        var confidence: Int? = null
    ) {
        enum class QuestionResponseType {
            ANSWERED, SKIPPED, MARKED
        }
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
        questionResponses = arrayOfNulls<QuestionResponse>(5)

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
            val selectedQuestions = categoryQuestions.shuffled().take(1)
            questions.addAll(selectedQuestions)
        }

        return questions.shuffled()
    }

    private fun updateQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.text
        option1.text = question.option1
        option2.text = question.option2
        option3.text = question.option3
        option4.text = question.option4
        confidenceSeekBar.progress = 0

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
        if (currentQuestionIndex == questions.size - 1) {
            btnNext.text = "Finish"
            btnMark.text = "Mark"
            btnSkip.text = "Skip"
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnNext -> {
                questionResponses[currentQuestionIndex] = QuestionResponse(
                    QuestionResponse.QuestionResponseType.ANSWERED,
                    getSelectedOption(),
                    getConfidence()
                )
            }

            R.id.btnMark -> {
                questionResponses[currentQuestionIndex] = QuestionResponse(
                    QuestionResponse.QuestionResponseType.MARKED,
                    getSelectedOption(),
                    getConfidence()
                )
            }

            R.id.btnSkip -> {
                questionResponses[currentQuestionIndex] =
                    (QuestionResponse(QuestionResponse.QuestionResponseType.SKIPPED))
            }
        }

        if (currentQuestionIndex + 1 == questions.size) {
            if (v.id != R.id.btnMark) {
                finishTest()
            }
        } else {
            currentQuestionIndex += 1
            updateQuestion()
            btnNext.isEnabled = false
            btnMark.isEnabled = false
        }
    }

    private fun getSelectedOption(): Int? {
        if (!optionGroup.isSelected)
            return null

        return when (optionGroup.checkedRadioButtonId) {
            R.id.option1 -> 1
            R.id.option2 -> 2
            R.id.option3 -> 3
            R.id.option4 -> 4
            else -> throw Exception("Selected option invalid")
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView!!.id in listOf(R.id.option1, R.id.option2, R.id.option3, R.id.option4)) {
            btnNext.isEnabled = true
            btnMark.isEnabled = true
        }
    }

    private fun finishTest() {
        btnNext.isEnabled = false
        btnMark.isEnabled = false
        btnSkip.isEnabled = false

        val scores = calculateScores()
        val category1Score = scores["category 1"]
        val category2Score = scores["category 2"]
        val category3Score = scores["category 3"]
        val category4Score = scores["category 4"]

        val message = "Test complete with scores:\nCategory 1: $category1Score\n" +
                "Category 2: $category2Score \nCategory 3: $category3Score\n" +
                "Category 4: $category4Score"

        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateScores(): Map<String, Double> {
        var category1Score = 0.0
        var category2Score = 0.0
        var category3Score = 0.0
        var category4Score = 0.0

        for (i in questions.indices) {
            if (questionResponses[i]!!.responseType == QuestionResponse.QuestionResponseType.SKIPPED) {
                continue
            }

            val questionScore =
                if (questions[i].correctOption == questionResponses[i]!!.optionSelected) {
                    questionResponses[i]!!.confidence!!
                } else {
                    -questionResponses[i]!!.confidence!!
                }
            when (questions[i].category) {
                "category 1" -> category1Score += questionScore
                "category 2" -> category2Score += questionScore
                "category 3" -> category3Score += questionScore
                "category 4" -> category4Score += questionScore
            }

        }

        return mapOf(
            "category 1" to category1Score,
            "category 2" to category2Score,
            "category 3" to category3Score,
            "category 4" to category4Score
        )
    }

    private fun getConfidence(): Int {
        return confidenceSeekBar.progress + 1
    }
}
