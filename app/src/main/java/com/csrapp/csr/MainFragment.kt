package com.csrapp.csr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnTakeTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnViewStreams).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnTakeTest -> navController.navigate(R.id.action_mainFragment_to_testStepSelectonFragment)
            R.id.btnViewStreams -> navController.navigate(R.id.action_mainFragment_to_streamSelectionFragment)
        }
    }

}
