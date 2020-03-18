package com.csrapp.csr.ui.taketest.personalitytest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.databinding.FragmentPersonalityTestBinding
import com.csrapp.csr.utils.InjectorUtils

class PersonalityTestFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var viewModel: PersonalityTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personality_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val factory = InjectorUtils.providePersonalityTestViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(PersonalityTestViewModel::class.java)

        DataBindingUtil.setContentView<FragmentPersonalityTestBinding>(
            this.requireActivity(),
            R.layout.fragment_personality_test
        )
            .apply {
                this.lifecycleOwner = this@PersonalityTestFragment
                this.personalityViewModel = viewModel
            }
    }
}
