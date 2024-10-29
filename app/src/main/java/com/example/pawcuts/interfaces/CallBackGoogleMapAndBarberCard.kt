package com.example.pawcuts.interfaces

import com.example.pawcuts.models.Barber

interface CallBackGoogleMapAndBarberCard {
    fun show()
    fun BarberCard(barber: Barber)
}