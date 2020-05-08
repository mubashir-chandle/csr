package com.csrapp.csr.ui.taketest.personalitytest

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.databinding.FragmentPersonalityTestBinding
import com.csrapp.csr.nlu.NLUService.NLUError.*
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_personality_test.*

class PersonalityTestFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var viewModel: PersonalityTestViewModel
    private lateinit var binding: FragmentPersonalityTestBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var loadingDialog: AlertDialog? = null
    private lateinit var instructionsDialog: AlertDialog

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_personality_test, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val factory = InjectorUtils.providePersonalityTestViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(PersonalityTestViewModel::class.java)

        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_filename),
            MODE_PRIVATE
        )

        binding.personalityViewModel = viewModel
        binding.lifecycleOwner = this

        val testFinishObserver = Observer<Boolean> { testFinished ->
            if (testFinished) {
                saveScore()

                val testCompletionDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Personality Test Completed")
                    .setMessage("You have successfully completed the second step of the test.\n\nYou can now view your result.")
                    .setPositiveButton("Okay", null)
                    .create()
                testCompletionDialog.show()
                navController.navigateUp()
            }
        }
        viewModel.testFinished.observe(viewLifecycleOwner, testFinishObserver)

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Analyzing Text")
                    .setView(R.layout.dialog_loading)
                    .setCancelable(false)
                    .create()

                loadingDialog?.show()
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel.nluErrorOccurred.observe(viewLifecycleOwner) { errorOccurred ->
            when (errorOccurred) {
                INTERNET -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("An error occurred while connecting to the internet. Please check your internet connection.")
                        .setPositiveButton("Try Again") { _, _ ->
                            viewModel.onButtonNextClicked()
                        }
                        .setNegativeButton("Skip This Question") { _, _ ->
                            viewModel.skipCurrentQuestion()
                        }
                        .create()
                        .show()
                }
                BAD_RESPONSE -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("An error occurred while analyzing the text. Please try again.")
                        .setPositiveButton("Okay", null)
                        .setNegativeButton("Skip This Question") { _, _ ->
                            viewModel.skipCurrentQuestion()
                        }
                        .create()
                        .show()
                }
                INSUFFICIENT_INPUT -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Insufficient Input")
                        .setMessage("Please enter more text to analyze it.")
                        .setPositiveButton("Okay", null)
                        .setNegativeButton("Skip This Question") { _, _ ->
                            viewModel.skipCurrentQuestion()
                        }
                        .create()
                        .show()
                }
            }

            viewModel.currentQuestionIndex.observe(viewLifecycleOwner) {
                hideKeyboard()
            }
        }

        instructionsDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.instructions)
            .setMessage(R.string.personality_test_instructions)
            .setPositiveButton(R.string.okay, null)
            .create()

        // Use a slightly different instructions dialog at start.
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.instructions)
            .setMessage(R.string.personality_test_instructions)
            .setPositiveButton(R.string.start, null)
            .create()
            .show()

        fabPersonalityInstructions.setOnClickListener(this)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = view?.rootView?.windowToken
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun saveScore() {
        val streams = viewModel.getAllStreams()
        val skippedQuestions = viewModel.getQuestionsSkippedInEachStream()
        val questionAndResponseHolder = viewModel.getQuestionsAndResponses()
        val questionsPerStream = viewModel.questionsPerStream

        val result = PersonalityTestHelper.generateScore(
            streams,
            skippedQuestions,
            questionAndResponseHolder,
            questionsPerStream
        )

        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.shared_preferences_personality_test_completed), true)
            result.forEach { (stream, score) ->
                putInt(stream, score)
            }
            commit()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabPersonalityInstructions -> {
                instructionsDialog.show()
            }
        }
    }
}
