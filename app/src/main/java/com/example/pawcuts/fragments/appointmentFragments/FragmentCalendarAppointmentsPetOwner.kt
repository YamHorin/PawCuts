package com.example.pawcuts.fragments.appointmentFragments

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.App
import com.example.pawcuts.R
import com.example.pawcuts.adapters.ChooseTimeAdapter
import com.example.pawcuts.databinding.FragmentCalendarAppointmentsPetOwnerBinding
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackMakeAppointment
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.utilities.CalendarManager
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson

class FragmentCalendarAppointmentsPetOwner :Fragment() {
    private lateinit var binding:FragmentCalendarAppointmentsPetOwnerBinding
    private val adapter:ChooseTimeAdapter = ChooseTimeAdapter()
    private val gson:Gson = Gson()
    var callBackMakeAppointMent: CallBackMakeAppointment? = null
    private val calendarManager = CalendarManager.getInstance()
    private lateinit var barber: Barber
    private lateinit var petOwner: PetOwner

    companion object{
        fun init(barber: Barber , petOwner: PetOwner): FragmentCalendarAppointmentsPetOwner {
            val fragment = FragmentCalendarAppointmentsPetOwner()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("Barber",gson.toJson(barber) )
            args.putString("PetOwner" , gson.toJson(petOwner))
            fragment.arguments = args
            return fragment

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarAppointmentsPetOwnerBinding.inflate(inflater, container, false)
        barber =  gson.fromJson(arguments?.getString("Barber").toString() ,Barber::class.java)
        petOwner = gson.fromJson(arguments?.getString("PetOwner").toString() , PetOwner::class.java)
        adapter.petOwner = petOwner
        adapter.barber = barber
        adapter.callBackMakeAppointMent = callBackMakeAppointMent
        binding.appointmentsFragmentBarberRV.adapter = adapter
        binding.appointmentsFragmentBarberRV.layoutManager = LinearLayoutManager(requireContext())
        calendarManager.callBackFireStoreResultBarber = object :CallBackFireStoreResultBarber
        {
            override fun noEventsForUser() {
                adapter.listAppointments = emptyList()
                adapter.notifyDataSetChanged()
            }

            override fun userPetBarberHasEvents(result: QuerySnapshot) {
                val listEvents = mutableListOf<AppointmentBarber>()
                for (document in result.documents)
                {
                    listEvents.add(
                        AppointmentBarber.Builder()
                            .nameOwner(document.getString("owner").toString())
                            .type(calendarManager.makeTypeFromStrType(document.getString("type").toString()))
                            .time(document.getString("time").toString())
                            .namePet(document.getString("name").toString())
                            .infoAboutThePet(document.getString("info").toString())
                            .phone(document.getString("phone").toString())
                            .uidFireBasePetOwner(document.getString("uidPetOwner").toString())
                            .builder()
                    )
                }
                adapter.listAppointments = listEvents
                adapter.notifyDataSetChanged()
            }

        }
        val calendar: Calendar=Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        updateEvents(currentYear, currentMonth, currentDay)
        return binding.root
    }
    fun updateEvents(currentYear:Int, currentMonth:Int, currentDay:Int)
    {
        adapter.currentDay = currentDay
        adapter.currentMonth = currentMonth
        adapter.currentYear =currentYear
        calendarManager.getEventsFromCalendarBarber(barber.uidFireBase ,currentYear ,currentMonth ,currentDay)
    }


}