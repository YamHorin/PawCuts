package com.example.pawcuts.fragments.signUpFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView


class ProgressBarSingUpScreenFragment : Fragment() {

    private lateinit var fragmentProgressBarSingUpScreen_EFAB_next : ExtendedFloatingActionButton
    private lateinit var fragmentProgressBarSingUpScreen_SIV_progressBar : ShapeableImageView
    var callBackNextButtonPress: CallBackSingUpScreenButtonsPress?  = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_progress_bar_sign_up_screen, container, false)
        findViews(v)
        initViews(v)
        return v
    }

    private fun findViews(v: View) {
        fragmentProgressBarSingUpScreen_EFAB_next = v.findViewById(R.id.FragmentProgressBarSingUpScreen_EFAB_next)
        fragmentProgressBarSingUpScreen_SIV_progressBar = v.findViewById(R.id.FragmentProgressBarSingUpScreen_SIV_progressBar)

    }

    private fun initViews(v: View) {
        fragmentProgressBarSingUpScreen_EFAB_next.setOnClickListener({nextButtonClick()})

    }

    private fun nextButtonClick() {
        callBackNextButtonPress?.nextScreen()
    }

    fun progressBarChange(stepNumber :Int)
    {
        when(stepNumber)
        {
            1 -> fragmentProgressBarSingUpScreen_SIV_progressBar.setImageResource(R.drawable.dot_part1)//no sure that work
            2 -> fragmentProgressBarSingUpScreen_SIV_progressBar.setImageResource(R.drawable.dot_part2)//no sure that work
            3 -> fragmentProgressBarSingUpScreen_SIV_progressBar.setImageResource(R.drawable.dot_part3)//no sure that work
        }
    }

}