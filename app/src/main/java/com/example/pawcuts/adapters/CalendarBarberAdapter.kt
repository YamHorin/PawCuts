package com.example.pawcuts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcuts.databinding.CalandarItemBarberScreenBinding
import com.example.pawcuts.interfaces.CallBackCancelButtonPress
import com.example.pawcuts.interfaces.CallBackShowPetInfoCardAndCreateEventBarberScreen
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.PetOwnerType

class CalendarBarberAdapter (
    var events:List<AppointmentBarber>  = emptyList<AppointmentBarber>()
): RecyclerView.Adapter<CalendarBarberAdapter.EventViewHolder>() {
    var callBackCancelButtonPress: CallBackCancelButtonPress? = null
    var callBackShowPetInfocardAndCreateEventBarberScreen: CallBackShowPetInfoCardAndCreateEventBarberScreen? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val binding = CalandarItemBarberScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return events.size
    }
    fun getItem(number:Int) = events.get(number)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        with(holder) {
            with(events.get(position))
            {
                binding.appointmentItemBarberMTVInfo.text  =infoAboutThePet
                binding.appointmentItemBarberMTVTime.text = time
                when(type)
                {
                    PetOwnerType.dog -> binding.appointmentItemBarberMTVName.text  = "$namePet (dog)"
                    PetOwnerType.cat -> binding.appointmentItemBarberMTVName.text  = "$namePet (cat)"
                    PetOwnerType.both -> {
                    binding.appointmentItemBarberMTVName.text  = "$namePet (cat and dog)"
                    binding.appointmentItemBarberMTVName.textSize = 20F
                    }
                    PetOwnerType.none -> namePet
                }
                binding.reviewCVData.setOnClickListener{
                    callBackShowPetInfocardAndCreateEventBarberScreen?.showInfo(this)
                }
            }

        }
    }
    inner class EventViewHolder (val binding: CalandarItemBarberScreenBinding) :
        RecyclerView.ViewHolder(binding.root)
        {
            init {
                binding.appointmentItemBarberFABCancel.setOnClickListener({callBackCancelButtonPress?.cancel(
                    getItem(adapterPosition)
                )})
            }
        }



}