package com.example.pawcuts.fragments.petOwnerFragments

import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.pawcuts.R
import com.example.pawcuts.databinding.FragmentProfileUserBinding
import com.example.pawcuts.fragments.barberFragments.ProfileBarberFragment
import com.example.pawcuts.interfaces.CallBackGeocodeListener
import com.example.pawcuts.interfaces.CallBackUpdateProfile
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson


class ProfileUserFragment : Fragment(),InterfaceFragments {
    private lateinit var binding:FragmentProfileUserBinding
    private val gson: Gson = Gson()
    private var address:String = "same as before"
    var isTheProfilePhotoChanged:Boolean =false
    private lateinit var petOwnerObject:PetOwner

    private var imageUri: Uri = Uri.EMPTY
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            imageUri  = uri
            isTheProfilePhotoChanged = true
            binding.ProfileUserFragmentACIVProfile.setImageURI(imageUri)
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    var callBackUpdateProfile: CallBackUpdateProfile?= null

    companion object{
        fun init(user: PetOwner): ProfileUserFragment {
            val fragment = ProfileUserFragment()
            val args = Bundle()
            val gson:Gson =Gson()
            args.putString("PetOwner",gson.toJson(user) )
            fragment.arguments = args
            return fragment


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileUserBinding.inflate(inflater, container, false)
        val barberStr  =arguments?.getString("PetOwner").toString()
        val petOwner:PetOwner = gson.fromJson(barberStr,PetOwner::class.java)
        petOwnerObject = petOwner
        //password
        binding.ProfileUserFragmentTIETPassWord.setText("same password")

        //update photo
        Glide.with(binding.root.context)
            .load(petOwner.profilePhoto)
            .centerCrop()
            .placeholder(R.drawable.add_a_photo)
            .into(binding.ProfileUserFragmentACIVProfile)
        //update name
        binding.ProfileUserFragmentTIETName.setText(petOwnerObject.name)
        //update phone
        binding.ProfileUserFragmentTIELPhone.setText(petOwnerObject.phoneNumber)
        //update price
        binding.ProfileUserFragmentTIELPetName.setText("${petOwnerObject.petName}")
        //update email
        binding.ProfileUserFragmentTIETEmail.setText(petOwnerObject.email)
        //update type
        when(petOwnerObject.animalType)
        {
            PetOwnerType.dog -> binding.ProfileUserFragmentRBDogBarber.isChecked =true
            PetOwnerType.cat -> binding.ProfileUserFragmentRBCatBarber.isChecked =true
            PetOwnerType.both -> binding.ProfileUserFragmentRBBoth.isChecked =true
            PetOwnerType.none -> return null
        }
        binding.ProfileUserFragmentACIVProfile.setOnClickListener({
            Log.d("upload image" ,"start now")
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })
        binding.LoginActivityBUTTONSave.setOnClickListener({callBackUpdateProfile?.saveChanges()})
        return binding.root
    }


    fun getUserInput(): PetOwner
    {
        var photoUrl =petOwnerObject.profilePhoto
        if (isTheProfilePhotoChanged)
            photoUrl =""


        var type: PetOwnerType = PetOwnerType.none
        when {
            binding.ProfileUserFragmentRBDogBarber.isChecked -> type = PetOwnerType.dog
            binding.ProfileUserFragmentRBCatBarber.isChecked -> type =PetOwnerType.cat
            binding.ProfileUserFragmentRBBoth.isChecked -> type =PetOwnerType.both
        }
        return PetOwner.Builder()
            .name(binding.ProfileUserFragmentTIETName.text.toString())
            .phoneNumber(binding.ProfileUserFragmentTIELPhone.text.toString())
            .email(binding.ProfileUserFragmentTIETEmail.text.toString())
            .petName(binding.ProfileUserFragmentTIELPetName.text.toString())
            .animalType(type)
            .moreDetails(binding.ProfileUserFragmentTIETInfo.text.toString())
            .profilePhoto(photoUrl)
            .uidFireBase(petOwnerObject.uidFireBase)
            .builder()

    }

    override fun checkUserInput() {
        if (
            binding.ProfileUserFragmentTIETName.text.toString() ==""||
            binding.ProfileUserFragmentTIELPhone.text.toString()==""||
            binding.ProfileUserFragmentTIETEmail.text.toString()==""||
            binding.ProfileUserFragmentTIELPetName.text.toString()==""
        )
            throw IllegalArgumentException()
        var type: PetOwnerType = PetOwnerType.none
        when {
            binding.ProfileUserFragmentRBDogBarber.isChecked -> type = PetOwnerType.dog
            binding.ProfileUserFragmentRBCatBarber.isChecked -> type = PetOwnerType.cat
            binding.ProfileUserFragmentRBBoth.isChecked -> type = PetOwnerType.both
        }
        if (type==PetOwnerType.none)
            throw IllegalArgumentException()
    }

    fun getUserProfile(): Uri {
        return imageUri
    }
    fun getPassword(): String? {
        val str = binding.ProfileUserFragmentTIETPassWord.text.toString()
        if (str=="same password")
            return null
        return str
    }


}