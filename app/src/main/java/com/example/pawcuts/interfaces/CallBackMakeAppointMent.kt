package com.example.pawcuts.interfaces

import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.PetOwner

interface CallBackMakeAppointment {
    fun make(barber: Barber , petOwner: PetOwner , time:String,
             day:Int, month:Int , year:Int)
}