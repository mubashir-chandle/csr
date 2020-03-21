package com.csrapp.csr.ui.taketest.personalitytest

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.databinding.FragmentPersonalityTestBinding
import com.csrapp.csr.utils.InjectorUtils

class PersonalityTestFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var viewModel: PersonalityTestViewModel
    private lateinit var binding: FragmentPersonalityTestBinding

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

        binding.personalityViewModel = viewModel
        binding.lifecycleOwner = this

        val testFinishObserver = Observer<Boolean> { testFinished ->
            if (testFinished) {
                val result = mutableMapOf<String, Double>()
                viewModel.getStreams().forEach { stream ->
                    result[stream] = 0.0
                }

                val questionsAndResponses = viewModel.getQuestionsAndResponses()
                questionsAndResponses.forEach { questionAndResponse ->
                    val question = questionAndResponse.question
                    if (question.type == "textual") {
                        // TODO: Use sentiment analysis.
                        val previousScore = result[question.stream]!!
                        result[question.stream!!] = previousScore
                    } else {
                        val previousScore = result[question.stream]!!
                        result[question.stream!!] =
                            previousScore + questionAndResponse.responseValue!!
                    }
                }

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
    }
}
