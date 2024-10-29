package com.example.pawcuts.interfaces

import com.example.pawcuts.models.AppointmentBarber

interface CallBackShowPetInfoCardAndCreateEventBarberScreen {
    fun showInfo(event: AppointmentBarber)
    fun showScreenAddEvent()
    fun AddEventBarberScreen()
}