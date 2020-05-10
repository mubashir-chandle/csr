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

        val personalityScores = mutableListOf<RecommendationResultItem>()
        viewModel.getAllStreams().forEach { streamEntity ->
            val recommendationIntensity = sharedPreferences.getFloat(streamEntity.id, 0.0f)

            if (recommendationIntensity != 0f) {
                personalityScores.add(
                    RecommendationResultItem(
                        streamEntity.id,
                        streamEntity.title,
                        streamEntity.description,
                        recommendationIntensity
                    )
                )
            }
            personalityScores.sortWith(Comparator { o1, o2 ->
                when {
                    // If both the streams have RI of 1, or both have RI of < 1, sort by their title
                    (o1.recommendationIntensity == 1f && o2.recommendationIntensity == 1f)
                            || (o1.recommendationIntensity < 1f && o2.recommendationIntensity < 1f) -> {
                        o1.title.compareTo(o2.title)
                    }
                    // Else, only one of the stream will have RI of 1. So, display it earilier.
                    else -> {
                        if (o1.recommendationIntensity == 1f)
                            -1
                        else
                            1
                    }
                }
            })
        }
        val personalityScoresAdapter =
            RecommendedStreamAdapter(personalityScores, requireContext(), navController)

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
