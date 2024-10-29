package com.example.pawcuts.interfaces

import com.example.pawcuts.models.Barber

interface CallBackShowBarberCardAndFavoriteBarber {
    fun show(barber: Barber)
    fun favorite(barber: Barber, position: Int)
}