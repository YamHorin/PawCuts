package com.example.pawcuts.fragments.barberFragments

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
import com.example.pawcuts.databinding.FragmentProfileBarberBinding
import com.example.pawcuts.interfaces.CallBackGeocodeListener
import com.example.pawcuts.interfaces.CallBackUpdateProfile
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson


class ProfileBarberFragment : Fragment() , InterfaceFragments {
    private lateinit var binding: FragmentProfileBarberBinding
    private val gson: Gson = Gson()
    var callBackGeocodeListener: CallBackGeocodeListener? = null
    private var address: String = "same address"
    var isTheProfilePhotoChanged: Boolean = false
    private lateinit var barberObj: Barber
    private var imageUri: Uri = Uri.EMPTY
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                imageUri = uri
                isTheProfilePhotoChanged = true
                binding.profileBarberFragmentACIVProfile.setImageURI(imageUri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    var callBackUpdateProfile: CallBackUpdateProfile? = null

    companion object {
        fun init(barberItself: Barber): ProfileBarberFragment {
            val fragment = ProfileBarberFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("Barber", gson.toJson(barberItself))
            fragment.arguments = args
            return fragment


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //make binding
        binding = FragmentProfileBarberBinding.inflate(inflater, container, false)
        val barberStr = arguments?.getString("Barber").toString()
        val barber: Barber = gson.fromJson(barberStr, Barber::class.java)
        barberObj = barber
        //update photo
        Glide.with(binding.root.context)
            .load(barber.profilePhoto)
            .centerCrop()
            .placeholder(R.drawable.add_a_photo)
            .into(binding.profileBarberFragmentACIVProfile)
        //password

        binding.profileBarberFragmentTIETPassword.setText("same password")
        //address
        getLocationFromLatitudeAndLongitude(barberObj.location)
        binding.profileBarberFragmentTIELAddress.setText(address)
        //update name
        binding.profileBarberFragmentTIETName.setText(barber.name)
        //update phone
        binding.profileBarberFragmentTIELPhone.setText(barber.phoneNumber)
        //update price
        binding.profileBarberFragmentTIETPrice.setText("${barber.price}")
        //update email
        binding.profileBarberFragmentTIETEmail.setText(barber.email)
        //update type
        when (barber.type) {
            BarberType.catBarber -> binding.profileBarberFragmentRBCatBarber.isChecked = true
            BarberType.dogBarber -> binding.profileBarberFragmentRBDogBarber.isChecked = true
            BarberType.catAndDogBarber -> binding.profileBarberFragmentRBBoth.isChecked = true
            BarberType.none -> binding.profileBarberFragmentRBCatBarber.isChecked = true
        }
        binding.profileBarberFragmentACIVProfile.setOnClickListener({
            Log.d("upload image", "start now")
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })
        binding.LoginActivityFABSave.setOnClickListener({ callBackUpdateProfile?.saveChanges() })
        return binding.root
    }

    override fun checkUserInput() {
        if (
            binding.profileBarberFragmentTIETName.text.toString() == "" ||
            binding.profileBarberFragmentTIELPhone.text.toString() == "" ||
            binding.profileBarberFragmentTIETEmail.text.toString() == "" ||
            binding.profileBarberFragmentTIELAddress.text.toString() == "" ||
            binding.profileBarberFragmentTIETPrice.text.toString() == ""
        )
            throw IllegalArgumentException()
        var type: BarberType = BarberType.none
        when {
            binding.profileBarberFragmentRBDogBarber.isChecked -> type = BarberType.dogBarber
            binding.profileBarberFragmentRBCatBarber.isChecked -> type = BarberType.catBarber
            binding.profileBarberFragmentRBBoth.isChecked -> type = BarberType.catAndDogBarber
        }
        if (type == BarberType.none)
            throw IllegalArgumentException()
    }

    fun getUserProfile(): Uri {
        return imageUri

    }

    fun getUserInput(): Barber {
        var photoUrl = barberObj.profilePhoto
        if (isTheProfilePhotoChanged)
            photoUrl = ""
        var location: String = ""
        if (binding.profileBarberFragmentTIELAddress.text.toString() == "same address")
            location = barberObj.location
        else
            getLocationFromStr(binding.profileBarberFragmentTIELAddress.text.toString())

        var type: BarberType = BarberType.none
        when {
            binding.profileBarberFragmentRBDogBarber.isChecked -> type = BarberType.dogBarber
            binding.profileBarberFragmentRBCatBarber.isChecked -> type = BarberType.catBarber
            binding.profileBarberFragmentRBBoth.isChecked -> type = BarberType.catAndDogBarber
        }
        return Barber.Builder()
            .name(binding.profileBarberFragmentTIETName.text.toString())
            .phoneNumber(binding.profileBarberFragmentTIELPhone.text.toString())
            .email(binding.profileBarberFragmentTIETEmail.text.toString())
            .price(binding.profileBarberFragmentTIETPrice.text.toString().toInt())
            .type(type)
            .location(location)
            .moreDetails(binding.profileBarberFragmentTIETInfo.text.toString())
            .profilePhoto(photoUrl)
            .uidFireBase(barberObj.uidFireBase)
            .builder()

    }

    private fun getLocationFromStr(locationStr: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val geocoder = Geocoder(binding.root.context)
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

    private fun getLocationFromLatitudeAndLongitude(jsonString: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val geocoder = Geocoder(binding.root.context)
            val location: LatLng = gson.fromJson(jsonString, LatLng::class.java)
            geocoder.getFromLocation(location.latitude, location.longitude, 5,
                Geocoder.GeocodeListener { addresses ->
                    if (addresses.isEmpty()) {
                        address = addresses[0].getAddressLine(0)
                        Log.d("getLocationFromLatitudeAndLongitude", address)
                    } else
                        Log.e("getLocationFromLatitudeAndLongitude", "No addresses found")
                }


            )
        }
    }

    fun getPassword(): String? {
        val str = binding.profileBarberFragmentTIETPassword.text.toString()
        if (str == "same password")
            return null
        return str
    }
}
