package com.example.pawcuts.fragments.appointmentFragments

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackBackButtonPress
import com.example.pawcuts.interfaces.CallBackCalendar
import com.example.pawcuts.utilities.CalendarManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CalendarAloneFragment : Fragment() {
    private lateinit var CalendarAloneFragment_CV: CalendarView

    private lateinit var CalendarAloneFragment_FAB_goBack: FloatingActionButton
    var CallBackCalendar: CallBackCalendar?=null
    private lateinit var calendar: Calendar
    var callBackBackButtonPress:CallBackBackButtonPress? =null
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_calendar_alone, container, false)
        CalendarAloneFragment_CV =v.findViewById(R.id.CalendarAloneFragment_CV)
        CalendarAloneFragment_FAB_goBack = v.findViewById(R.id.CalendarAloneFragment_FAB_goBack)
        CalendarAloneFragment_FAB_goBack.setOnClickListener({callBackBackButtonPress?.goBack()})
        calendar = Calendar.getInstance()
        CalendarAloneFragment_CV.setOnDateChangeListener(object : CalendarView.OnDateChangeListener
        {
            override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {
                currentDay = day
                currentMonth = month+1
                currentYear = year
                CallBackCalendar?.showEvents(currentYear , currentMonth,currentDay )
            }

        })

        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val milli:Long = calendar.timeInMillis
        CalendarAloneFragment_CV.date = milli
         return v
        // Inflate the layout for this fragment
    }


}