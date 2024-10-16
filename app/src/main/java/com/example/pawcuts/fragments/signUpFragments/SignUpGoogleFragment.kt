package com.example.pawcuts.fragments.signUpFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.databinding.FragmentSignUpGoogleBinding
import com.example.pawcuts.models.SignUpPhases
import com.example.pawcuts.models.UserType


class SignUpGoogleFragment : Fragment(), InterfaceFragments {
    private lateinit var binding: FragmentSignUpGoogleBinding
    private val linkToPicture = "https://wildwash.co.uk/wp-content/uploads/2022/05/Corgi-enjoying-a-shampoo-bath-1000px.jpg"
    var callBackSingUpScreenButtonsPress: CallBackSingUpScreenButtonsPress?  =null
    val screenName: SignUpPhases = SignUpPhases.GoogleAccount
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpGoogleBinding.inflate(inflater, container, false)
        Glide.with(binding.root).load(linkToPicture).centerCrop().placeholder(R.drawable.ic_launcher_background).into(binding.SignUpGoogleFragmentACIVImageDoggy)
        binding.SignUpGoogleFragmentFABGoBack.setOnClickListener({callBackSingUpScreenButtonsPress?.goBackScreen(screenName)})
        return binding.root
    }
    fun getUserType(): UserType {
        when{
            binding.SignUpGoogleFragmentRBPetOwner.isChecked-> return UserType.petOwner
            binding.SingnUpGoogleFragmentRBPetBarber.isChecked-> return UserType.petBarber
        }
        return UserType.none
    }

    override fun checkUserInput() {
        var userType:String =""
        if(binding.SignUpGoogleFragmentRBPetOwner.isChecked)
            userType = "barber"
        if(binding.SingnUpGoogleFragmentRBPetBarber.isChecked)
            userType = "owner"
        if (userType=="")
            throw IllegalArgumentException()
    }

}