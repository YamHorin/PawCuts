package com.example.pawcuts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcuts.databinding.AppointmentOfPetOwnerItemBinding
import com.example.pawcuts.interfaces.CallBackCancelButtonPress
import com.example.pawcuts.models.AppointmentPetOwner

class CalendarPetOwnerAdapter(
    var events:List<AppointmentPetOwner> = emptyList<AppointmentPetOwner>()
):RecyclerView.Adapter<CalendarPetOwnerAdapter.EventViewHolder>() {
    var callBackCancelButtonPress:CallBackCancelButtonPress? =null
    inner class EventViewHolder(val binding:AppointmentOfPetOwnerItemBinding):
    RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.appointmentItemBarberFABCancel.setOnClickListener({callBackCancelButtonPress?.cancel(getItem(adapterPosition))})
        }
    }

    private fun getItem(adapterPosition: Int): AppointmentPetOwner {
        return events[adapterPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = AppointmentOfPetOwnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
       with(holder)
       {
           with(events[position])
           {
               binding.appointmentPetOwnerMTVNameBarber.text  = nameBarber
               binding.appointmentItemBarberMTVTime.text = time
           }
       }
    }
}