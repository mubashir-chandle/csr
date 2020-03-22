package com.csrapp.csr.ui.taketest.result

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment(), View.OnClickListener {
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

        btnViewJobs.setOnClickListener(this)

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preference_filename),
            Context.MODE_PRIVATE
        )

        val isAptitudeTestCompleted = sharedPreferences.getBoolean("isAptitudeTestCompleted", false)
        val scores = mutableMapOf<String, Double>()

        if (isAptitudeTestCompleted) {
            viewModel.getAptitudeCategories().forEach { category ->
                scores[category] = sharedPreferences.getFloat(category, 0f).toDouble()
            }
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
