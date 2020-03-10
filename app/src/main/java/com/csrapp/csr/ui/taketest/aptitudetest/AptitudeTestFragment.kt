package com.csrapp.csr.ui.taketest.aptitudetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.data.AptitudeQuestionEntity
import com.csrapp.csr.utils.InjectorUtils

class AptitudeTestFragment : Fragment() {
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

        initUI()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideAptitudeTestViewModelFactory(activity!!)
        val viewModel = ViewModelProvider(this, factory).get(AptitudeTestViewModel::class.java)

        val categories = viewModel.getAptitudeCategories()

        val questions = mutableListOf<AptitudeQuestionEntity>()
        categories.forEach { category ->
            val categoryQuestions = viewModel.getAptitudeQuestionsByCategory(category)
            val selectedQuestions = categoryQuestions.shuffled().take(5)
            questions.addAll(selectedQuestions)
        }
    }
}
