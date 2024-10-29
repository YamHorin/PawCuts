package com.example.pawcuts.fragments.barberFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pawcuts.R
import com.example.pawcuts.databinding.FragmentMakeEventBarberBinding
import com.example.pawcuts.interfaces.CallBackShowPetInfoCardAndCreateEventBarberScreen
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.PetOwnerType


class MakeEventBarberFragment : Fragment() ,InterfaceFragments {
 private lateinit var binding:FragmentMakeEventBarberBinding
 var callBackEvent:CallBackShowPetInfoCardAndCreateEventBarberScreen? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakeEventBarberBinding.inflate(inflater, container, false)
        binding.MakeEventFABSave.setOnClickListener({callBackEvent?.AddEventBarberScreen()})
        return binding.root
    }

    override fun checkUserInput() {
        checkDateFromStr(binding.MakeEventBarberFragmentTIETDate.text.toString())
        checkTimeFromStr(binding.MakeEventBarberFragmentTIETStartTime.text.toString())
        checkTimeFromStr(binding.MakeEventBarberFragmentTIETFinishTime.text.toString())
    }
    fun getUserDate(): ArrayList<Int> {
        return  getDateFromStr(binding.MakeEventBarberFragmentTIETDate.text.toString())
    }
    fun getEvents(): MutableList<AppointmentBarber> {
        val startTime:Int = binding.MakeEventBarberFragmentTIETStartTime.text.toString().substring(0,2).toInt()
        var finishTime:Int = binding.MakeEventBarberFragmentTIETFinishTime.text.toString().substring(0,2).toInt()
        if (binding.MakeEventBarberFragmentTIETFinishTime.text.toString().substring(3,5).toInt()>0)
            finishTime+=1
        val eventsInfo = binding.MakeEventBarberFragmentTIETInfo.text.toString()
        val listEvents = mutableListOf<AppointmentBarber>()
        for (i in startTime..<finishTime)
        {
            listEvents.add(
                AppointmentBarber.Builder()
                    .time("$i:00-${i+1}:00")
                    .type(PetOwnerType.none)
                    .namePet(eventsInfo)
                    .builder()
            )
        }
        return listEvents

    }
    private fun checkDateFromStr(string: String)
    {
        for (char in string)
        {
            if (!char.isDigit() && char!= '.')
                throw IllegalArgumentException()
        }
    }

    private fun checkTimeFromStr(string: String)
    {
        if (string=="")
            throw IllegalArgumentException()
        for (char in string)
        {
            if (!char.isDigit() && char!= ':')
                throw IllegalArgumentException()
        }
    }
    private fun getDateFromStr(string: String): ArrayList<Int> {
        val arrDate = ArrayList<Int>()
        //day
        arrDate.add(string.substring(0,2).toInt())
        //month
        arrDate.add(string.substring(3,5).toInt())
        //year
        arrDate.add(string.substring(6,10).toInt())

        return arrDate
    }

}