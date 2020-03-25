package com.csrapp.csr.ui.taketest.aptitudetest

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_aptitude_test.*
import kotlin.math.roundToInt

class AptitudeTestFragment : Fragment(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
    AdapterView.OnItemSelectedListener {

    private val TAG = "AptitudeTestFragment"

    private lateinit var navController: NavController
    private lateinit var viewModel: AptitudeTestViewModel
    private lateinit var spinnerAdapter: SpinnerQuestionAdapter
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backConfirmationDialog = AlertDialog.Builder(requireContext())
            .setTitle("Quit Test")
            .setMessage("Are you sure you want to quit the test?")
            .setPositiveButton("Yes") { _, _ ->
                navController.navigateUp()
            }
            .setNegativeButton("Cancel") { _, _ ->
                println("Cancel pressed")
            }
            .create()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            backConfirmationDialog.show()
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

        val factory = InjectorUtils.provideAptitudeTestViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(AptitudeTestViewModel::class.java)

        spinnerAdapter = viewModel.getSpinnerAdapter(requireContext())
        spinnerQuestions.adapter = spinnerAdapter

        viewModel.testFinished.observe(this) { testFinished ->
            if (testFinished) {
                finishTest(finishedByTimer = true)
            }
        }

        viewModel.timeRemaining.observe(this) {
            val minutes = it / 60
            val seconds = it % 60
            remainingTime.text = "$minutes:$seconds"
        }

        assignActionListeners()
        updateButtons()
    }

    private fun assignActionListeners() {
        btnNext.setOnClickListener(this)
        btnMark.setOnClickListener(this)
        btnClear.setOnClickListener(this)

        optionGroup.setOnCheckedChangeListener(this)

        spinnerQuestions.onItemSelectedListener = this

        confidenceSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun updateUI() {
        layout.smoothScrollTo(0, 0)
        spinnerQuestions.setSelection(viewModel.currentQuestionIndex)

        val questionHolder = spinnerAdapter.getItem(viewModel.currentQuestionIndex)!!
        val question = questionHolder.question
        questionText.text = question.text

        if (question.referenceImage.isNullOrBlank()) {
            referenceImage.visibility = View.GONE
        } else {
            referenceImage.visibility = View.VISIBLE
            val identifier = requireContext().resources.getIdentifier(
                question.referenceImage,
                "mipmap",
                requireContext().packageName
            )
            referenceImage.setImageResource(identifier)
        }

        option1.text = question.option1
        option2.text = question.option2
        option3.text = question.option3
        option4.text = question.option4

        // Check if this question was answered previously.
        if (questionHolder.optionSelected == null) {
            optionGroup.clearCheck()
        } else {
            updateConfidenceTextView()
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

        updateConfidenceTextView()
        updateButtons()
    }

    private fun updateButtons() {
        if (optionGroup.checkedRadioButtonId == -1) {
            confidenceSeekBar.isEnabled = false
            btnMark.isEnabled = false
            btnClear.isEnabled = false
        } else {
            confidenceSeekBar.isEnabled = true
            updateConfidenceTextView()
            btnMark.isEnabled = true
            btnClear.isEnabled = true
        }

        if (viewModel.currentQuestionIndex == spinnerAdapter.count - 1) {
            btnNext.text = "Finish"
            btnMark.text = "Mark"
        } else {
            btnNext.text = "Next"
            btnMark.text = "Mark and Next"
        }
    }

    private fun finishTest(finishedByTimer: Boolean) {
        btnNext.isEnabled = false
        btnMark.isEnabled = false
        btnClear.isEnabled = false

        saveScores()

        val message = when (finishedByTimer) {
            true -> "Your time for the aptitude test has finished.\n" +
                    "\n" +
                    "You can now start the second step whenever you are ready for it."

            false -> "You have successfully completed the first step of the test.\n" +
                    "\n" +
                    "You can now start the second step whenever you are ready for it."
        }

        val testCompletionDialog = AlertDialog.Builder(requireContext())
            .setTitle("Aptitude Test Completed")
            .setMessage(message)
            .setPositiveButton("Okay", null)
            .create()
        testCompletionDialog.show()
        navController.navigateUp()
    }

    private fun saveScores() {
        val scores = mutableMapOf<String, Double>()
        val questionsPerCategory = viewModel.questionsPerCategory

        for (i in 0 until spinnerAdapter.count) {
            val questionHolder = spinnerAdapter.getItem(i)!!
            if (questionHolder.responseType == QuestionHolder.QuestionResponseType.UNANSWERED)
                continue

            val questionScore =
                if (questionHolder.question.correctOption == questionHolder.optionSelected) {
                    // Convert to double to avoid integer division.
                    (questionHolder.confidence!!).toDouble() / questionsPerCategory
                } else {
                    (-questionHolder.confidence!!).toDouble() / questionsPerCategory
                }

            val previousScore = scores[questionHolder.question.category] ?: 0.0
            scores[questionHolder.question.category] = previousScore + questionScore
        }

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_filename),
            MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.shared_preferences_aptitude_test_completed), true)
            scores.forEach { (category, score) ->
                putInt(category, score.roundToInt())
                Log.d(TAG, "$category: ${score.roundToInt()}")
            }
            commit()
        }
    }

    // Next/Mark/Skip Button clicked.
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnNext -> {
                if (getSelectedOption() != null) {
                    val questionHolder = spinnerAdapter.getItem(viewModel.currentQuestionIndex)!!
                    questionHolder.responseType = QuestionHolder.QuestionResponseType.ANSWERED
                    questionHolder.optionSelected = getSelectedOption()
                    questionHolder.confidence = confidenceSeekBar.progress

                }

                // If not the last question.
                if (viewModel.currentQuestionIndex != spinnerAdapter.count - 1) {
                    viewModel.currentQuestionIndex += 1
                    updateUI()
                } else {
                    finishTest(finishedByTimer = false)
                }
            }

            R.id.btnMark -> {
                if (getSelectedOption() != null) {
                    val questionHolder = spinnerAdapter.getItem(viewModel.currentQuestionIndex)!!
                    questionHolder.responseType = QuestionHolder.QuestionResponseType.MARKED
                    questionHolder.optionSelected = getSelectedOption()
                    questionHolder.confidence = confidenceSeekBar.progress
                }

                // If not the last question.
                if (viewModel.currentQuestionIndex != spinnerAdapter.count - 1) {
                    viewModel.currentQuestionIndex += 1
                    updateUI()
                } else {
                    // Change the color of question number in spinner manually in the last question.
                    spinnerAdapter.notifyDataSetChanged()
                }
            }

            R.id.btnClear -> {
                val questionHolder = spinnerAdapter.getItem(viewModel.currentQuestionIndex)!!
                questionHolder.responseType = QuestionHolder.QuestionResponseType.UNANSWERED
                questionHolder.optionSelected = null
                questionHolder.confidence = 0

                optionGroup.clearCheck()

                // Update to confidence TextView must be done after clearing option group.
                confidenceSeekBar.progress = 0
                updateConfidenceTextView()

                // Change the color of question number in spinner.
                spinnerAdapter.notifyDataSetChanged()
            }
        }
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
        viewModel.currentQuestionIndex = position
        updateUI()
    }

    // Answer option selected.
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        updateButtons()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        updateConfidenceTextView()
    }

    private fun updateConfidenceTextView() {
        if (getSelectedOption() != null) {
            textViewConfidence.visibility = View.VISIBLE
            val percent = confidenceSeekBar.progress
            textViewConfidence.text = "${percent}%"
        } else {
            textViewConfidence.visibility = View.INVISIBLE
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Do Nothing.
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Do Nothing.
    }
}
