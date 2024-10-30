package com.example.pawcuts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcuts.databinding.BarberItemBinding
import com.example.pawcuts.databinding.CalendarItemPetOwnerScreenBinding
import com.example.pawcuts.interfaces.CallBackMakeAppointment
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.PetOwner

class ChooseTimeAdapter: RecyclerView.Adapter<ChooseTimeAdapter.CardViewHolder>()  {
    var callBackMakeAppointMent: CallBackMakeAppointment?= null
    var barber:Barber? =null
    var petOwner:PetOwner? =null
    var currentYear = 0
    var currentMonth = 0
    var currentDay = 0
    var listAppointmentsUser:List<AppointmentBarber> = emptyList()
    var listAppointments:List<AppointmentBarber> = emptyList()
    inner class CardViewHolder(val binding:CalendarItemPetOwnerScreenBinding) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.reviewCVData.setOnClickListener({callBackMakeAppointMent?.make(
                    barber!! , petOwner!! ,
                    binding.appointmentItemBarberMTVTime.text.toString()
                    , currentDay ,currentMonth, currentYear

                )})
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = CalendarItemPetOwnerScreenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        val listEventsFree:List<Int>  = FindAvailableEvents()
        return listEventsFree.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val listEventsFree:List<Int>  = FindAvailableEvents()
        listEventsFree.sorted()
        with (holder)
        {
            with(listEventsFree[position])
            {
                "$this:00 - ${this+1}:00".also { binding.appointmentItemBarberMTVTime.text = it }
            }
        }
    }

    private fun findIndexInList(time: String , listHoursStr:MutableList<String>): Int {
        for (str in listHoursStr)
            if (str==time)
                return listHoursStr.indexOf(str)
        return (listHoursStr.size)-2
    }

    private fun FindAvailableEvents(): List<Int> {
        val listHoursInt = mutableListOf<Int>()
        val listHoursStr = mutableListOf<String>()
        for (i in 8..21)
        {
            listHoursStr.add("$i:00 - ${i+1}:00")
            listHoursInt.add(i)
        }
        for (appointment in listAppointments)
        {
            val num = findIndexInList((appointment.time) ,listHoursStr)
            listHoursInt.removeAt(num)
            listHoursStr.removeAt(num)
        }
        for (appointment in listAppointmentsUser)
        {
            val num = findIndexInList((appointment.time) ,listHoursStr)
            listHoursInt.removeAt(num)
            listHoursStr.removeAt(num)
        }
        return listHoursInt
    }
}