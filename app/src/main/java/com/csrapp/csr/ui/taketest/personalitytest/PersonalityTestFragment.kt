package com.csrapp.csr.ui.taketest.personalitytest

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.databinding.FragmentPersonalityTestBinding
import com.csrapp.csr.utils.InjectorUtils

class PersonalityTestFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var viewModel: PersonalityTestViewModel
    private lateinit var binding: FragmentPersonalityTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_personality_test, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val factory = InjectorUtils.providePersonalityTestViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(PersonalityTestViewModel::class.java)

        binding.personalityViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.testFinished.observe(this) { testFinished ->
            if (testFinished) {
                val testCompletionDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Personality Test Completed")
                    .setMessage("You have successfully completed the second step of the test.\n\nYou can now view your result.")
                    .setPositiveButton("Okay", null)
                    .create()
                testCompletionDialog.show()
                navController.navigateUp()
            }

        }
    }
}
