package com.csrapp.csr.ui.taketest.aptitudetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_aptitude_test.*

class AptitudeTestFragment : Fragment(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: AptitudeTestViewModel
    private lateinit var spinnerAdapter: SpinnerQuestionAdapter
    private var currentQuestionIndex = 0

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

        val factory = InjectorUtils.provideAptitudeTestViewModelFactory(activity!!)
        viewModel = ViewModelProvider(this, factory).get(AptitudeTestViewModel::class.java)

        spinnerAdapter = SpinnerQuestionAdapter(context!!, viewModel.getRandomQuestions())
        spinnerQuestions.adapter = spinnerAdapter

        assignActionListeners()
        updateButtons()
    }

    private fun assignActionListeners() {
        btnNext.setOnClickListener(this)
        btnMark.setOnClickListener(this)
        btnSkip.setOnClickListener(this)

        optionGroup.setOnCheckedChangeListener(this)

        spinnerQuestions.onItemSelectedListener = this
    }

    private fun updateUI() {
        layout.smoothScrollTo(0, 0)
        spinnerQuestions.setSelection(currentQuestionIndex)

        val questionHolder = spinnerAdapter.getItem(currentQuestionIndex)!!
        val question = questionHolder.question
        questionText.text = question.text

        if (questionHolder.responseType == QuestionHolder.QuestionResponseType.UNVISITED)
            questionHolder.responseType = QuestionHolder.QuestionResponseType.SKIPPED

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

        option1.text = question.option1
        option2.text = question.option2
        option3.text = question.option3
        option4.text = question.option4

        // Check if this question was answered previously.
        if (questionHolder.optionSelected == null)
            optionGroup.clearCheck()
        else {
            when (questionHolder.optionSelected) {
                1 -> optionGroup.check(R.id.option1)
                2 -> optionGroup.check(R.id.option2)
                3 -> optionGroup.check(R.id.option3)
                4 -> optionGroup.check(R.id.option4)
            }
        }

        if (questionHolder.confidence == null)
            confidenceSeekBar.progress = 0
        else
            confidenceSeekBar.progress = questionHolder.confidence!! - 1

        updateButtons()
    }

    private fun updateButtons() {
        btnSkip.visibility = View.VISIBLE

        if (optionGroup.checkedRadioButtonId == -1) {
            confidenceSeekBar.isEnabled = false
            btnNext.isEnabled = false
            btnMark.isEnabled = false
        } else {
            confidenceSeekBar.isEnabled = true
            btnNext.isEnabled = true
            btnMark.isEnabled = true
        }

        if (currentQuestionIndex == spinnerAdapter.count - 1) {
            btnNext.text = "Finish"
            btnMark.text = "Mark"
            btnSkip.text = "Clear"
        } else {
            btnNext.text = "Next"
            btnMark.text = "Mark and Next"
            btnSkip.text = "Skip This Question"
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

        for (i in 0 until spinnerAdapter.count) {
            val questionHolder = spinnerAdapter.getItem(i)!!
            if (questionHolder.responseType == QuestionHolder.QuestionResponseType.SKIPPED ||
                questionHolder.responseType == QuestionHolder.QuestionResponseType.UNVISITED
            ) {
                continue
            }

            val questionScore =
                if (questionHolder.question.correctOption == questionHolder.optionSelected) {
                    questionHolder.confidence!!
                } else {
                    -questionHolder.confidence!!
                }
            when (questionHolder.question.category) {
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

    private fun getSelectedOption(): Int? {
        if (optionGroup.checkedRadioButtonId == -1)
            return null

        return when (optionGroup.checkedRadioButtonId) {
            R.id.option1 -> 1
            R.id.option2 -> 2
            R.id.option3 -> 3
            R.id.option4 -> 4
            else -> throw Exception("Selected option invalid")
        }
    }


    // Spinner item selected.
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do Nothing.
    }

    // Spinner item selected.
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currentQuestionIndex = position
        updateUI()
    }

    // Answer option selected.
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        updateButtons()
    }

    // Next/Mark/Skip Button clicked.
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnNext -> {
                val questionHolder = spinnerAdapter.getItem(currentQuestionIndex)!!
                questionHolder.responseType = QuestionHolder.QuestionResponseType.ANSWERED
                questionHolder.optionSelected = getSelectedOption()
                questionHolder.confidence = getConfidence()
            }

            R.id.btnMark -> {
                val questionHolder = spinnerAdapter.getItem(currentQuestionIndex)!!
                questionHolder.responseType = QuestionHolder.QuestionResponseType.MARKED
                questionHolder.optionSelected = getSelectedOption()
                questionHolder.confidence = getConfidence()
            }

            R.id.btnSkip -> {
                val questionHolder = spinnerAdapter.getItem(currentQuestionIndex)!!
                questionHolder.responseType = QuestionHolder.QuestionResponseType.SKIPPED
                questionHolder.optionSelected = null
                questionHolder.confidence = null
            }
        }

        if (currentQuestionIndex + 1 == spinnerAdapter.count) {
            if (v.id == R.id.btnNext) {
                finishTest()
            } else if (v.id == R.id.btnSkip) {
                optionGroup.clearCheck()
                confidenceSeekBar.progress = 0
            }
        } else {
            currentQuestionIndex += 1
            updateUI()
        }
    }
}
