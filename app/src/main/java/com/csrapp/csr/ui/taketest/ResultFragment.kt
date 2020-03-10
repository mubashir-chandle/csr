package com.csrapp.csr.ui.taketest

import android.os.Bundle
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
