package com.csrapp.csr.ui.taketest.result

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var viewModel: ResultViewModel
    private lateinit var instructionsDialog: AlertDialog

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

        val aptitudeScores = mutableListOf<AptitudeResultItem>()
        viewModel.getAllCategories().forEach { categoryEntity ->
            val score = sharedPreferences.getInt(categoryEntity.id, 0)
            aptitudeScores.add(
                AptitudeResultItem(
                    categoryEntity.id,
                    categoryEntity.title,
                    categoryEntity.description,
                    score
                )
            )
        }
        val aptitudeScoresAdapter = AptitudeResultAdapter(aptitudeScores, requireContext())

        recyclerViewAptitudeScores.apply {
            adapter = aptitudeScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }

        val yesStreams = mutableListOf<RecommendationResultItem>()
        val maybeStreams = mutableListOf<RecommendationResultItem>()
        viewModel.getAllStreams().forEach { streamEntity ->
            val recommendationResult = RecommendationResult.intToRecommendationResult(
                sharedPreferences.getInt(
                    streamEntity.id,
                    0
                )
            )

            if (recommendationResult == RecommendationResult.Yes) {
                yesStreams.add(
                    RecommendationResultItem(
                        streamEntity.id,
                        streamEntity.title,
                        streamEntity.description,
                        recommendationResult
                    )
                )
            } else if (recommendationResult == RecommendationResult.Maybe) {
                maybeStreams.add(
                    RecommendationResultItem(
                        streamEntity.id,
                        streamEntity.title,
                        streamEntity.description,
                        recommendationResult
                    )
                )
            }

        }
        val allResults = mutableListOf<RecommendationResultItem>()
        yesStreams.forEach {
            allResults.add(it)
        }
        maybeStreams.forEach {
            allResults.add(it)
        }

        val personalityScoresAdapter =
            RecommendedStreamAdapter(allResults, requireContext(), navController)

        recyclerViewPersonalityScores.apply {
            adapter = personalityScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }

        instructionsDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.instructions)
            .setMessage(R.string.view_result_instructions)
            .setPositiveButton(R.string.okay, null)
            .create()

        instructionsDialog.show()
        fabResultInstructions.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabResultInstructions -> {
                instructionsDialog.show()
            }
        }
    }
}
