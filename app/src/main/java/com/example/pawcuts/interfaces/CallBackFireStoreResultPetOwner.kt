package com.example.pawcuts.interfaces

import com.google.firebase.firestore.QuerySnapshot

interface CallBackFireStoreResultPetOwner {
    fun showEvents(result: QuerySnapshot)
    fun noEvents()
}