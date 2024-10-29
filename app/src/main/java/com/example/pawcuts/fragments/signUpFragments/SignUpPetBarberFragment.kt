package com.example.pawcuts.fragments.signUpFragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.databinding.FragmentSignUpPetBarberBinding
import com.example.pawcuts.interfaces.CallBackGeocodeListener
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.SignUpPhases
import com.example.pawcuts.models.BarberType
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.runBlocking


class SignUpPetBarberFragment: Fragment(),InterfaceFragments {
    private lateinit var binding: FragmentSignUpPetBarberBinding
    private lateinit var geocoder :Geocoder
    var callBackSingUpScreenButtonsPress:CallBackSingUpScreenButtonsPress? = null
    var callBackGeocodeListener: CallBackGeocodeListener? = null
    val screenName: SignUpPhases = SignUpPhases.petBarber
    private var imageUri: Uri = Uri.EMPTY
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            imageUri  = uri
            binding.SingUpPetBarberFragmentACIVProfile.setImageURI(imageUri)
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpPetBarberBinding.inflate(inflater, container, false)
        geocoder = Geocoder(binding.root.context)
        binding.SignUpPetBarberFragmentFABGoBack.setOnClickListener({callBackSingUpScreenButtonsPress?.goBackScreen(screenName)})
        binding.SingUpPetBarberFragmentACIVProfile.setOnClickListener({updatePicture()})
        return binding.root
    }

    fun getUserInfoPetBarber(emailUser:String , uidUser:String): Barber {
        //profile pic- later fireBase will give an url to put
        val profilePic = "URL will get from FireBase"
        //get Location
        val locationStr:String = binding.SingUpPetBarberFragmentTIELAddress.text.toString()
        getLocationFromStr(locationStr)
        //get name
        val name  = binding.SingUpPetBarberFragmentTIETName.text.toString()
        //get price
        val price  =binding.SingUpPetBarberFragmentTIETPrice.text.toString().toInt()
        //get phone number
        val phone = binding.SingUpPetBarberFragmentTIELPhone.text.toString()
        //get Type
        var type: BarberType = BarberType.none
        when {
            binding.SingUpPetBarberFragmentRBDogBarber.isChecked -> type = BarberType.dogBarber
            binding.SingUpPetBarberFragmentRBCatBarber.isChecked -> type = BarberType.catBarber
            binding.SingUpPetBarberFragmentRBBoth.isChecked -> type = BarberType.catAndDogBarber
        }
        val barber:Barber = Barber
            .Builder()
            .name(name)
            .email(emailUser)
            .type(type)
            .phoneNumber(phone)
            .price(price)
            .profilePhoto(profilePic)
            .location("LatLng(0.0,0.0)")
            .uidFireBase(uidUser)
            .moreDetails("")
            .builder()
        Log.d("getUserInfoPetBarber" , barber.toString())
        return barber
    }

    private fun getLocationFromStr(locationStr: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(locationStr, 5,
                Geocoder.GeocodeListener { addresses ->
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val latitude = address.latitude
                        val longitude = address.longitude
                        Log.d("getLocationFromStr", "Latitude: $latitude, Longitude: $longitude")
                        callBackGeocodeListener?.onGeocodeSuccess(LatLng(latitude, longitude))
                    } else {
                        Log.e("getLocationFromStr", "No addresses found")
                        callBackGeocodeListener?.onGeocodeFailure(Exception("No addresses found"))
                    }
                }
            )
        }

    }

    private fun updatePicture()
    {
        Log.d("upload image" ,"start now")
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    fun getUserProfileByteMap() = drawableToBitmap(binding.SingUpPetBarberFragmentACIVProfile.drawable)

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

    override fun checkUserInput() {
        if (binding.SingUpPetBarberFragmentTIELAddress.text.toString() == ""||
            binding.SingUpPetBarberFragmentTIETName.text.toString()==""||
            binding.SingUpPetBarberFragmentTIETPrice.text.toString()==""||
            binding.SingUpPetBarberFragmentTIELPhone.text.toString()=="")
            throw IllegalArgumentException()

        var type: BarberType = BarberType.none
        when {
            binding.SingUpPetBarberFragmentRBDogBarber.isChecked -> type = BarberType.dogBarber
            binding.SingUpPetBarberFragmentRBCatBarber.isChecked -> type = BarberType.catBarber
            binding.SingUpPetBarberFragmentRBBoth.isChecked -> type = BarberType.catAndDogBarber
        }
        if (type==BarberType.none)
            throw IllegalArgumentException()

    }
    fun getUserProfile(): Uri {
        return imageUri

    }


}