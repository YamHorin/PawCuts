package com.example.pawcuts.interfaces

import com.google.firebase.firestore.QuerySnapshot

interface CallBackFireStoreResultBarber {
    fun noEventsForUser()
    fun userPetBarberHasEvents(result: QuerySnapshot)
}