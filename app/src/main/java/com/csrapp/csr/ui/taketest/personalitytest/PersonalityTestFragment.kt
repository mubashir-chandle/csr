package com.csrapp.csr.ui.taketest.personalitytest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import com.csrapp.csr.databinding.FragmentPersonalityTestBinding
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_personality_test.*

class PersonalityTestFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
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

        assignObservers()
        questionResponseSlider.setOnSeekBarChangeListener(this)
    }

    private fun assignObservers() {
        viewModel.currentQuestionIndex.observe(this) {
            updateUI()
        }
    }

    private fun updateUI() {
        val question = viewModel.getCurrentQuestion()
//        questionText.text = question.text

        if (question.type == "slider") {
            questionResponseSlider.visibility = View.VISIBLE
            questionResponseText.visibility = View.GONE
        } else {
            questionResponseText.visibility = View.VISIBLE
            questionResponseSlider.visibility = View.GONE
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        sliderValue.text = "${getSliderValue()}%"
    }

    private fun getSliderValue() = (questionResponseSlider.progress + 1) * 10

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Do nothing.
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Do nothing.
    }
}
