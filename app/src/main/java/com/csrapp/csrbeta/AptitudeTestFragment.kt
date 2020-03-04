package com.csrapp.csrbeta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation

class AptitudeTestFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aptitude_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnPersonalityTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnGoBackToTestSelection).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnPersonalityTest -> navController.navigate(R.id.action_aptitudeTestFragment_to_personalityTestFragment)
            R.id.btnGoBackToTestSelection -> activity!!.onBackPressed()
        }
    }

}
