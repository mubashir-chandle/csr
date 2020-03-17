package com.csrapp.csr.ui.taketest.personalitytest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.fragment_personality_test.*

class PersonalityTestFragment : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private lateinit var navController: NavController

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

        btnNext.setOnClickListener(this)
        questionResponseSlider.setOnSeekBarChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnNext -> TODO()
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
