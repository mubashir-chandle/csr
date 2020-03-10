package com.csrapp.csr.ui.taketest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.fragment_test_step_selection.*

class TestStepSelectionFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController

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

        btnAptitudeTest.setOnClickListener(this)
        btnPersonalityTest.setOnClickListener(this)
        btnViewResult.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAptitudeTest -> navController.navigate(
                R.id.action_testStepSelectonFragment_to_aptitudeTestFragment
            )
            R.id.btnPersonalityTest -> navController.navigate(
                R.id.action_testStepSelectonFragment_to_personalityTestFragment
            )
            R.id.btnViewResult -> navController.navigate(
                R.id.action_testStepSelectonFragment_to_resultFragment
            )
        }
    }
}
