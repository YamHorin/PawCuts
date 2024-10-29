package com.example.pawcuts.fragments.barberFragments

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.adapters.CalendarBarberAdapter
import com.example.pawcuts.databinding.FragmentCalendarAppointmentsPetBarberBinding
import com.example.pawcuts.interfaces.CallBackCancelButtonPress
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackShowPetInfoCardAndCreateEventBarberScreen
import com.example.pawcuts.models.AppointmentBarber

import com.example.pawcuts.utilities.CalendarManager
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson

class CalendarAppointmentsPetBarberFragment: Fragment() {
    private lateinit var binding: FragmentCalendarAppointmentsPetBarberBinding
    private lateinit var calendar:Calendar
    private lateinit var calendarManager: CalendarManager
    private lateinit var uidFireBase:String
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private lateinit var adapter: CalendarBarberAdapter
    private val gson:Gson = Gson()

    var callBackShowPetInfoCardAndCreateEventBarberScreen:CallBackShowPetInfoCardAndCreateEventBarberScreen? = null
    companion object{
        fun init(uidFireBaseBarber:String): CalendarAppointmentsPetBarberFragment {
            val fragment = CalendarAppointmentsPetBarberFragment()
            val args = Bundle()
            args.putString("uid", uidFireBaseBarber)
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
        binding =FragmentCalendarAppointmentsPetBarberBinding.inflate(inflater, container, false)
        binding.CalendarAppointmentsPetBarberFragmentFABAddEvent.setOnClickListener({callBackShowPetInfoCardAndCreateEventBarberScreen?.showScreenAddEvent()})
        calendar = Calendar.getInstance()

        calendarManager.callBackFireStoreResultBarber = object : CallBackFireStoreResultBarber {
            override fun noEventsForUser() {
                adapter.events = emptyList<AppointmentBarber>()
                adapter.notifyDataSetChanged()
            }

            override fun userPetBarberHasEvents(result: QuerySnapshot) {
                Log.d("getEventsFromCalendarBarber" ,"result = "+result.toString())
                val list: MutableList<AppointmentBarber> = mutableListOf()
                for (document in result) {
                    list.add(
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
                //set list to the events of the day
                adapter.events = list
                adapter.notifyDataSetChanged()
            }

        }
        binding.appointmentsFragmentCalendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener
        {
            override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {
                currentDay = day
                currentMonth = month+1
                currentYear = year
                calendarManager.getEventsFromCalendarBarber(uidFireBase , day = currentDay , month =currentMonth , year = currentYear)
            }

        })
        //set date
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        adapter = CalendarBarberAdapter()
        adapter.callBackCancelButtonPress = object:CallBackCancelButtonPress{
            override fun cancel(item: Any) {
                if (item is AppointmentBarber)
                {
                    calendarManager.cancelEvent(uidFireBase, item.uidFireBasePetOwner,item.time , currentYear , currentMonth ,currentDay)
                    //update adapter
                    calendarManager.getEventsFromCalendarBarber(uidFireBase , day = currentDay , month =currentMonth , year = currentYear)
                }
            }
        }
        adapter.callBackShowPetInfocardAndCreateEventBarberScreen = callBackShowPetInfoCardAndCreateEventBarberScreen
        val milli:Long = calendar.timeInMillis
        binding.appointmentsFragmentCalendarView.date = milli
        //set list to the events of the day
        calendarManager.getEventsFromCalendarBarber(uidFireBase , day = currentDay , month =currentMonth , year = currentYear)
        binding.appointmentsFragmentBarberRV.adapter = adapter
        binding.appointmentsFragmentBarberRV.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }


}