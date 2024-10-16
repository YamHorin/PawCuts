package com.example.pawcuts.fragments.signUpFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.SignUpPhases
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class SignUpMoreDetailsFragment : Fragment(), InterfaceFragments {

    private lateinit var singUpMoreDetailsFragment_FAB_goBack: FloatingActionButton
    private lateinit var singUpPetBarberFragment_TIET_MD : TextInputEditText
    var callBackSingUpScreenButtonsPress:CallBackSingUpScreenButtonsPress?  =null
    val screenName:SignUpPhases = SignUpPhases.moreDetails
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_sign_up_more_details, container, false)
        findViews(v)
        initViews(v)
        return v
    }
    private fun initViews(v: View) {
        singUpMoreDetailsFragment_FAB_goBack.setOnClickListener({callBackSingUpScreenButtonsPress?.goBackScreen(screenName)})
    }

    private fun findViews(v: View) {
        singUpMoreDetailsFragment_FAB_goBack = v.findViewById(R.id.SingUpMoreDetailsFragment_FAB_goBack)
        singUpPetBarberFragment_TIET_MD = v.findViewById(R.id.SingUpMoreDetailsFragment_TIET_MoreInfo)
    }
    fun getMoreDetails(): String {
        Log.d("getMoreDetails" ,singUpPetBarberFragment_TIET_MD.text.toString())
        return singUpPetBarberFragment_TIET_MD.text.toString()
    }

    override fun checkUserInput() {
        if (singUpPetBarberFragment_TIET_MD.text.toString()=="")
            throw IllegalArgumentException()
    }

}