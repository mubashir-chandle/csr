package com.csrapp.csr.ui.taketest.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            getString(R.string.shared_preference_filename),
            Context.MODE_PRIVATE
        )

        val isAptitudeTestCompleted = sharedPreferences.getBoolean("isAptitudeTestCompleted", false)
        val isPersonalityTestCompleted =
            sharedPreferences.getBoolean("isPersonalityTestCompleted", false)

        if (!isAptitudeTestCompleted || !isPersonalityTestCompleted) {
            Toast.makeText(
                requireContext(),
                "Please complete both the steps of the test first!",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigateUp()
        }

        val aptitudeScores = mutableListOf<ResultItem>()
        viewModel.getAptitudeCategories().forEach { category ->
            val score = sharedPreferences.getInt(category, 0)
            aptitudeScores.add(ResultItem(category, score))
        }
        Log.d(TAG, aptitudeScores.toString())
        val aptitudeScoresAdapter = ResultAdapter()
        aptitudeScoresAdapter.setUpItems(aptitudeScores)

        recyclerViewAptitudeScores.apply {
            adapter = aptitudeScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }

        val personalityScores = mutableListOf<ResultItem>()
        viewModel.getAllStreams().forEach { streamEntity ->
            val streamId = streamEntity.id
            val streamTitle = viewModel.getStreamTitleFromId(streamId)
            val score = sharedPreferences.getFloat(streamId, 0f).toDouble()

            personalityScores.add(ResultItem(streamTitle, score.toInt()))
        }
        Log.d(TAG, personalityScores.toString())
        val personalityScoresAdapter = ResultAdapter()
        personalityScoresAdapter.setUpItems(personalityScores)

        recyclerViewPersonalityScores.apply {
            adapter = personalityScoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }
}
