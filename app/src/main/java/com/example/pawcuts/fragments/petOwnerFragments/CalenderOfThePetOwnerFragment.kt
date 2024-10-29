package com.example.pawcuts.fragments.petOwnerFragments

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.adapters.CalendarPetOwnerAdapter
import com.example.pawcuts.databinding.FragmentCalendarOfThePetOwnerBinding
import com.example.pawcuts.fragments.barberFragments.CalendarAppointmentsPetBarberFragment
import com.example.pawcuts.interfaces.CallBackCancelButtonPress
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackFireStoreResultPetOwner
import com.example.pawcuts.models.AppointmentPetOwner
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.utilities.CalendarManager
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson

class CalenderOfThePetOwnerFragment :Fragment() {
    private lateinit var binding:FragmentCalendarOfThePetOwnerBinding
    private lateinit var calendar: Calendar
    private lateinit var calendarManager: CalendarManager
    private lateinit var uidFireBase:String
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private lateinit var adapter: CalendarPetOwnerAdapter
    private val gson: Gson = Gson()
    companion object{
        fun init(uidFireBasePetOwner:String): CalenderOfThePetOwnerFragment {
            val fragment = CalenderOfThePetOwnerFragment()
            val args = Bundle()
            args.putString("uid", uidFireBasePetOwner)
            fragment.arguments = args
            return fragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarManager = CalendarManager.getInstance()
        uidFireBase = arguments?.getString("uid" ).toString()
        binding = FragmentCalendarOfThePetOwnerBinding.inflate(inflater, container, false)
        calendar = Calendar.getInstance()
        calendarManager.callBackFireStoreResultPetOwner = object : CallBackFireStoreResultPetOwner {
            override fun showEvents(result: QuerySnapshot) {
                val listEvents   = mutableListOf<AppointmentPetOwner>()
                for (doc in result.documents)
                {
                    listEvents.add(
                        AppointmentPetOwner.Builder()
                            .time(doc.getString("time").toString())
                            .type(calendarManager.petOwnerMakeTypeFromStrType(doc.getString("type").toString()))
                            .nameBarber(doc.getString("name").toString())
                            .uidFireBaseBarber(doc.getString("uid").toString())
                            .builder()
                    )
                }
                adapter.events = listEvents
                adapter.notifyDataSetChanged()
            }

            override fun noEvents() {
                adapter.events = emptyList()
                adapter.notifyDataSetChanged()
            }

        }
        binding.appointmentsFragmentCalendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener
        {
            override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {
                currentDay = day
                currentMonth = month+1// Months are 0-based in Calendar
                currentYear = year
                calendarManager.getEventsFromCalendarPetOwner(uidFireBase , day = currentDay , month =currentMonth , year = currentYear)
            }

        })
        //set date
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        adapter = CalendarPetOwnerAdapter()
        binding.appointmentsFragmentBarberRV.adapter = adapter
        binding.appointmentsFragmentBarberRV.layoutManager = LinearLayoutManager(requireContext())
        adapter.callBackCancelButtonPress = object:CallBackCancelButtonPress{
            override fun cancel(item: Any) {
                if (item is AppointmentPetOwner)
                {
                    calendarManager.cancelEvent(item.uidFireBaseBarber,uidFireBase, item.time , currentYear , currentMonth ,currentDay)
                    calendarManager.getEventsFromCalendarBarber(uidFireBase , day = currentDay , month =currentMonth , year = currentYear)

                }
            }
        }
        return binding.root
    }








}

