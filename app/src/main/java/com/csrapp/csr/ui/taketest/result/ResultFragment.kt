package com.csrapp.csr.ui.taketest.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {
    private val TAG = "ResultFragment"

    private lateinit var navController: NavController
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val factory = InjectorUtils.provideResultViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(ResultViewModel::class.java)

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_filename),
            Context.MODE_PRIVATE
        )

        val isAptitudeTestCompleted = sharedPreferences.getBoolean(
            getString(R.string.shared_preferences_aptitude_test_completed),
            false
        )
        val isPersonalityTestCompleted =
            sharedPreferences.getBoolean(
                getString(R.string.shared_preferences_personality_test_completed),
                false
            )

        if (!isAptitudeTestCompleted || !isPersonalityTestCompleted) {
            throw Exception("Result Screen started before completing both the test steps")
        }

        val aptitudeScores = mutableListOf<ResultItem>()
        viewModel.getAllCategories().forEach { categoryEntity ->
            val score = sharedPreferences.getInt(categoryEntity.id, 0)
            aptitudeScores.add(
                ResultItem(
                    ResultItem.ResultItemType.APTITUDE,
                    categoryEntity.id,
                    categoryEntity.title,
                    categoryEntity.description,
                    score
                )
            )
        }
        Log.d(TAG, "Aptitude scores: $aptitudeScores")
        val aptitudeScoresAdapter = ResultAdapter(aptitudeScores, requireContext(), navController)

        recyclerViewAptitudeScores.apply {
            adapter = aptitudeScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }

        val personalityScores = mutableListOf<ResultItem>()
        viewModel.getAllStreams().forEach { streamEntity ->
            val score = sharedPreferences.getInt(streamEntity.id, 0)

            personalityScores.add(
                ResultItem(
                    ResultItem.ResultItemType.PERSONALITY,
                    streamEntity.id,
                    streamEntity.title,
                    streamEntity.description,
                    score
                )
            )
        }
        Log.d(TAG, "Personality scores: $personalityScores")
        val personalityScoresAdapter =
            ResultAdapter(personalityScores, requireContext(), navController)

        recyclerViewPersonalityScores.apply {
            adapter = personalityScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }
}
