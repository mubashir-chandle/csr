package com.csrapp.csr.ui.taketest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment(), View.OnClickListener {
    private val TAG = "ResultFragment"

    private lateinit var navController: NavController

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

        btnViewJobs.setOnClickListener(this)

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preference_filename),
            Context.MODE_PRIVATE
        )

        val isAptitudeTestCompleted = sharedPreferences.getBoolean("isAptitudeTestCompleted", false)

        if (isAptitudeTestCompleted) {
            val category1Score = sharedPreferences.getFloat("category 1", 0f).toDouble()
            val category2Score = sharedPreferences.getFloat("category 2", 0f).toDouble()
            val category3Score = sharedPreferences.getFloat("category 3", 0f).toDouble()
            val category4Score = sharedPreferences.getFloat("category 4", 0f).toDouble()

            Log.d(
                TAG,
                "Result of aptitude tests: $category1Score, $category2Score, $category3Score, $category4Score"
            )
        }
    }

    override fun onClick(v: View?) {
//        TODO: Change hardcoded stream
        when (v!!.id) {
            R.id.btnViewJobs -> {
                val bundle = bundleOf("stream" to "technical")
                navController.navigate(
                    R.id.action_resultFragment_to_jobSelectionFragment,
                    bundle
                )
            }
        }
    }
}
