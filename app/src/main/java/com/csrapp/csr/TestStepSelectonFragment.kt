package com.csrapp.csr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

class TestStepSelectonFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_step_selecton, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnAptitudeTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnPersonalityTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnViewResult).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAptitudeTest -> navController.navigate(R.id.action_testStepSelectonFragment_to_aptitudeTestFragment)
            R.id.btnPersonalityTest -> navController.navigate(R.id.action_testStepSelectonFragment_to_personalityTestFragment)
            R.id.btnViewResult -> navController.navigate(R.id.action_testStepSelectonFragment_to_resultFragment)
        }
    }
}
