package com.example.pawcuts.fragments.signUpFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.databinding.FragmentSignUpAllUsersBinding
import com.example.pawcuts.models.SignUpPhases

class SignUpAllUsersFragment : Fragment() ,InterfaceFragments {
    private lateinit var binding: FragmentSignUpAllUsersBinding
    private val linkToPicture:String = "https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg"
    val screenName:SignUpPhases = SignUpPhases.EmailAndPassword
    var callBackSingUpScreenButtonsPress: CallBackSingUpScreenButtonsPress?  =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpAllUsersBinding.inflate(inflater, container, false)
        //add picture
        binding.SingUpAllUsersFragmentEFABSignUpGoogle.setOnClickListener({callBackSingUpScreenButtonsPress?.signUpGoogle()})
        binding.SingUpAllUsersFragmentFABGoBack.setOnClickListener({callBackSingUpScreenButtonsPress?.goBackScreen(screenName)})
        return binding.root
    }

    fun getInfoUser(): Array<String> {
        val userEmail  = binding.SingUpAllUsersFragmentTILEmail.getEditText()?.getText().toString().trim();
        val userPassword = binding.SingUpAllUsersFragmentTILPassword.getEditText()?.getText().toString().trim();
        var userType:String =""
        if(binding.SingUpAllUsersFragmentRBPetBarber.isChecked)
            userType = "barber"
        if(binding.SingUpAllUsersFragmentRBPetOwner.isChecked)
            userType = "owner"
        return arrayOf(userEmail , userPassword , userType)
    }

    override fun checkUserInput() {
        if(binding.SingUpAllUsersFragmentTIETEmail.text.toString()=="" ||
            binding.SingUpAllUsersFragmentTIELPassword.text.toString() =="")
            throw IllegalArgumentException()
        var userType:String =""
        if(binding.SingUpAllUsersFragmentRBPetBarber.isChecked)
            userType = "barber"
        if(binding.SingUpAllUsersFragmentRBPetOwner.isChecked)
            userType = "owner"
        if (userType=="")
            throw IllegalArgumentException()
    }


}