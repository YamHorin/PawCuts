package com.example.pawcuts.fragments.signUpFragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.pawcuts.databinding.FragmentSignUpPetOwnerBinding
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.example.pawcuts.models.SignUpPhases
import com.google.android.gms.maps.model.LatLng


class SignUpPetOwnerFragment : Fragment() , InterfaceFragments {
    private lateinit var binding: FragmentSignUpPetOwnerBinding
    var callBackSingUpScreenButtonsPress: CallBackSingUpScreenButtonsPress? = null
    val screenName: SignUpPhases = SignUpPhases.petBarber
    private var imageUri:Uri = Uri.EMPTY
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            imageUri  = uri
            binding.SingUpPetOwnerFragmentACIVProfile.setImageURI(uri)
            Log.d("PhotoPicker", "Selected URI: $imageUri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentSignUpPetOwnerBinding.inflate(inflater, container, false)
        binding.SingUpPetOwnerFragmentACIVProfile.setOnClickListener({updatePicture()})
        binding.SingUpPetOwnerFragmentFABGoBack.setOnClickListener({callBackSingUpScreenButtonsPress?.goBackScreen(screenName)})
        return binding.root
    }
    fun getUserProfileByteMap() = drawableToBitmap(binding.SingUpPetOwnerFragmentACIVProfile.drawable)
    fun getUserProfile() = imageUri
    private fun drawableToBitmap(drawable: Drawable): Bitmap {

        val width =
            if (!drawable.bounds.isEmpty) drawable.bounds.width() else drawable.intrinsicWidth

        val height =
            if (!drawable.bounds.isEmpty) drawable.bounds.height() else drawable.intrinsicHeight

        // Now we check we are > 0
        val bitmap: Bitmap = Bitmap.createBitmap(
            if (width <= 0) 1 else width, if (height <= 0) 1 else height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun getUserInfoPetOwner(emailUser:String , uidUser:String): PetOwner {
        //profile pic- later fireBase will give an url to put
        val profilePic = ""
        //get name
        val name  = binding.SingUpPetOwnerFragmentTIETName.text.toString()
        //get price
        val petName = binding.SingUpPetOwnerFragmentTIETNamePet.text.toString()
        //get phone number
        val phone = binding.SingUpPetOwnerFragmentTIELPhone.text.toString()
        //get Type
        var type: PetOwnerType = PetOwnerType.none
        when {
            binding.SingUpPetOwnerFragmentRBDogOwner.isChecked -> type = PetOwnerType.dog
            binding.SingUpPetOwnerFragmentRBCat.isChecked-> type = PetOwnerType.cat
            binding.SingUpPetOwnerFragmentRBBothOwner.isChecked -> type = PetOwnerType.both
        }
        return PetOwner
            .Builder()
            .name(name)
            .petName(petName)
            .email(emailUser)
            .profilePhoto(profilePic)
            .phoneNumber(phone)
            .animalType(type)
            .uidFireBase(uidUser)
            .moreDetails("")
            .location("LatLng(0.0,0.0)")
            .builder()
    }

    override fun checkUserInput() {
        if (binding.SingUpPetOwnerFragmentTIETName.text.toString()==""||
            binding.SingUpPetOwnerFragmentTIETNamePet.text.toString()==""||
            binding.SingUpPetOwnerFragmentTIELPhone.text.toString()=="")
            throw IllegalArgumentException()

        var type: PetOwnerType = PetOwnerType.none
        when {
            binding.SingUpPetOwnerFragmentRBDogOwner.isChecked -> type = PetOwnerType.dog
            binding.SingUpPetOwnerFragmentRBCat.isChecked-> type = PetOwnerType.cat
            binding.SingUpPetOwnerFragmentRBBothOwner.isChecked -> type = PetOwnerType.both
        }
        if (type==PetOwnerType.none)
            throw IllegalArgumentException()
    }

    private fun updatePicture()
    {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }



}