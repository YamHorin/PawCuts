package com.example.pawcuts.fragments.barberFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackBackButtonPress
import com.example.pawcuts.interfaces.CallBackUploadImage
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.utilities.ImageProfileManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson


class ShowPetInfoFragment : Fragment() {

    private lateinit var ShowPetInfoFragment_FAB_GoBack: FloatingActionButton
    private lateinit var ShowPetInfoFragment_IMG_poster  : ShapeableImageView
    private lateinit var ShowPetInfoFragment_MTV_namePet : MaterialTextView
    private lateinit var ShowPetInfoFragment_MTV_nameOwner :MaterialTextView
    private lateinit var ShowPetInfoFragment_MTV_info:MaterialTextView
    private lateinit var  ShowPetInfoFragment_MTV_phone:MaterialTextView

    var callBackButtonBack: CallBackBackButtonPress? =null
    companion object
    {
        fun init(appointmentBarber: AppointmentBarber): ShowPetInfoFragment {
            val fragment = ShowPetInfoFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("event",gson.toJson(appointmentBarber) )
            fragment.arguments = args
            return fragment

        }


        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =inflater.inflate(R.layout.fragment_show_pet_info, container, false)
        findViews(v)
        val gson:Gson = Gson()
        val eventStr = arguments?.getString("event")
        val event:AppointmentBarber = gson.fromJson(eventStr  , AppointmentBarber::class.java)
        val imageManager:ImageProfileManager = ImageProfileManager.getInstance()
        imageManager.getImageURL(event.uidFireBasePetOwner )
        imageManager.callBackUploadImage = object :CallBackUploadImage
        {
            override fun onSuccess(downloadUrl: String) {
                Glide
                    .with(v.context)
                    .load(downloadUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(ShowPetInfoFragment_IMG_poster)
            }

            override fun onFailure(exception: Exception) {
                Glide
                    .with(v.context)
                    .load(R.drawable.logo)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(ShowPetInfoFragment_IMG_poster)
            }

        }
        initViews(v , event)
        return v
    }

    private fun initViews(v: View?, event: AppointmentBarber) {
        ShowPetInfoFragment_FAB_GoBack.setOnClickListener{callBackButtonBack?.goBack()}
        ShowPetInfoFragment_MTV_namePet.text = event.namePet
        ShowPetInfoFragment_MTV_nameOwner.text =event.nameOwner
        ShowPetInfoFragment_MTV_info.text = event.namePet
        ShowPetInfoFragment_MTV_phone.text  = event.phone
    }

    private fun findViews(v: View) {
        ShowPetInfoFragment_FAB_GoBack = v.findViewById(R.id.ShowPetInfoFragment_FAB_GoBack)
        ShowPetInfoFragment_IMG_poster = v.findViewById(R.id.ShowPetInfoFragment_IMG_poster)
        ShowPetInfoFragment_MTV_namePet = v.findViewById(R.id.ShowPetInfoFragment_MTV_namePet)
        ShowPetInfoFragment_MTV_nameOwner = v.findViewById(R.id.ShowPetInfoFragment_MTV_nameOwner)
        ShowPetInfoFragment_MTV_info = v.findViewById(R.id.ShowPetInfoFragment_MTV_info)
        ShowPetInfoFragment_MTV_phone = v.findViewById(R.id.ShowPetInfoFragment_MTV_phone)
    }


}