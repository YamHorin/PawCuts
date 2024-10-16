package com.example.pawcuts.interfaces

import com.example.pawcuts.models.SignUpPhases

interface CallBackSingUpScreenButtonsPress {
    fun nextScreen()
    fun goBackScreen(screenName:SignUpPhases)
    fun signUpGoogle()

}