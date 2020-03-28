package com.csrapp.csr.ui.taketest

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.fragment_test_step_selection.*

class TestStepSelectionFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_step_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_filename),
            MODE_PRIVATE
        )

        val isAptitudeTestCompleted = sharedPreferences.getBoolean(
            getString(R.string.shared_preferences_aptitude_test_completed),
            false
        )
        val isPersonalityTestCompleted = sharedPreferences.getBoolean(
            getString(R.string.shared_preferences_personality_test_completed),
            false
        )

        btnAptitudeTest.isEnabled = !isAptitudeTestCompleted
        btnPersonalityTest.isEnabled = isAptitudeTestCompleted && !isPersonalityTestCompleted
        btnResetProgress.isEnabled = isAptitudeTestCompleted
        btnViewResult.isEnabled = isPersonalityTestCompleted

        btnAptitudeTest.setOnClickListener(this)
        btnPersonalityTest.setOnClickListener(this)
        btnViewResult.setOnClickListener(this)
        btnResetProgress.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAptitudeTest -> navController.navigate(
                R.id.action_testStepSelectionFragment_to_aptitudeTestFragment
            )
            R.id.btnPersonalityTest -> {
                navController.navigate(
                    R.id.action_testStepSelectionFragment_to_personalityTestFragment
                )
            }
            R.id.btnViewResult -> {
                navController.navigate(
                    R.id.action_testStepSelectionFragment_to_resultFragment
                )
            }
            R.id.btnResetProgress -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Reset")
                    .setMessage("Are you sure you want to reset all your progress?")
                    .setPositiveButton("Yes") { _, _ ->
                        with(sharedPreferences.edit()) {
                            remove(getString(R.string.shared_preferences_aptitude_test_completed))
                            remove(getString(R.string.shared_preferences_personality_test_completed))
                            commit()
                        }
                        btnAptitudeTest.isEnabled = true
                        btnPersonalityTest.isEnabled = false
                        btnViewResult.isEnabled = false
                        btnResetProgress.isEnabled = false
                    }
                    .setNegativeButton("No", null)
                    .create()
                    .show()
            }
        }
    }
}
