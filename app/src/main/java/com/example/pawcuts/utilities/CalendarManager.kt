package com.example.pawcuts.utilities

import android.content.Context
import android.util.Log
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackFireStoreResultPetOwner
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.AppointmentPetOwner
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.example.pawcuts.models.UserType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class CalendarManager private constructor(context: Context) {
    val db = Firebase.firestore
    val gson:Gson =Gson()
    var callBackFireStoreResultBarber:CallBackFireStoreResultBarber? =null
    var callBackFireStoreResultPetOwner: CallBackFireStoreResultPetOwner? =null

    companion object {
        @Volatile
        private var instance: CalendarManager? = null

        fun init(context: Context): CalendarManager {
            return instance ?: synchronized(this) {

                instance ?: CalendarManager(context).also { instance = it }
            }
        }

        fun getInstance(): CalendarManager {
            return instance
                ?: throw IllegalStateException("CalendarManager must be initialized by calling init(context) before use.")
        }

    }

    fun getEventsFromCalendarPetOwner(uidFireBase:String , year:Int , month:Int,day:Int)
    {
        val docRef = db.collection("PetOwners").document(uidFireBase).collection("$day.$month.$year")
        //is the users has events?
        docRef.addSnapshotListener{  value, e ->
            if (value!=null ){
                Log.d("getEventsFromCalendarPetOwner" ,"result is  NOT empty :)${value}")
                callBackFireStoreResultPetOwner?.showEvents(value)
            }
            if (value==null){
                Log.d("getEventsFromCalendarPetOwner" ,"result is empty date:$day.$month.$year")
                callBackFireStoreResultPetOwner?.noEvents()
            }

        }

    }
    fun getEventsFromCalendarBarber(uidFireBase:String , year:Int , month:Int,day:Int)
    {
        val docRef = db.collection("PetBarbers").document(uidFireBase).collection("$day.$month.$year")
        //is the users has events?
        docRef.addSnapshotListener{  value, e ->
            if (value!=null ){
                Log.d("getEventsFromCalendarBarber" ,"result is  NOT empty :)${value}")
                callBackFireStoreResultBarber?.userPetBarberHasEvents(value)
            }
            if (value==null){
                Log.d("getEventsFromCalendarBarber" ,"result is empty date:$day.$month.$year")
                callBackFireStoreResultBarber?.noEventsForUser()
            }

        }

    }
    private fun createEventInPetOwnerCalendar(uidFireBase:String , year:Int , month:Int,day:Int,event:AppointmentPetOwner)
    {
        val data = hashMapOf(
            "time" to event.time,
            "name" to event.nameBarber,
            "uid" to event.uidFireBaseBarber,
            "type" to petOwnerMakeStrFromType(event.type)
        )
        db.collection("PetOwners").document(uidFireBase).collection("$day.$month.$year")
            .document(event.time).set(data).addOnSuccessListener { documentReference ->
                Log.d("createEventInPetOwnerCalendar", "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("createEventInPetOwnerCalendar", "Error adding document", e)
            }
    }
    fun createEventInPetBarberCalendar(uidFireBaseBarber:String , year:Int , month:Int,day:Int,event:AppointmentBarber)
    {

        db.collection("PetBarbers").document(uidFireBaseBarber).collection("$day.$month.$year").document(event.time).delete()
        val data = hashMapOf(
            "time" to event.time,
            "name" to event.namePet,
            "owner" to event.nameOwner,
            "phone" to event.phone,
            "info" to event.infoAboutThePet,
            "type" to makeStrFromType(event.type),
            "uidPetOwner" to event.uidFireBasePetOwner
        )
        db.collection("PetBarbers").document(uidFireBaseBarber).collection("$day.$month.$year")
            .document(event.time).set(data).addOnSuccessListener { documentReference ->
                Log.d("createEventInPetBarberCalendar", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("createEventInPetBarberCalendar", "Error adding document", e)
            }
    }


    fun createNewCalendarForNewUser(uidFireBase:String , userType: UserType)
    {
        when(userType)
        {
            UserType.petOwner -> {
            db.collection("PetOwners").add(emptyMap<String, Any>())
                .addOnSuccessListener { documentReference ->
                Log.d("createNewCalendarForNewUser", "DocumentSnapshot added with ID: ${documentReference.id}")
                 }
                .addOnFailureListener { e ->
                    Log.w("createNewCalendarForNewUser", "Error adding document", e)
                }

            }
            UserType.petBarber ->
            {
                db.collection("PetBarbers").add(emptyMap<String, Any>())
                    .addOnSuccessListener { documentReference ->
                        Log.d("createNewCalendarForNewUser", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("createNewCalendarForNewUser", "Error adding document", e)
                    }

            }
            UserType.none -> return
        }
    }

    fun cancelEvent(uidFireBaseBarber: String, uidFirePetOwner: String,time:String, year: Int, month: Int, day: Int) {
        //delete document from petOwner if it's not a private event of the barber
        if(uidFirePetOwner!="")
        {
            db.collection("PetOwners").document(uidFirePetOwner).collection("$day.$month.$year").document(time)
                .delete() .addOnSuccessListener { Log.d("cancelEvent", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("cancelEvent", "Error deleting document", e) }

        }
        //delete document from petBarber
        db.collection("PetBarbers").document(uidFireBaseBarber).collection("$day.$month.$year").document(time)
            .delete() .addOnSuccessListener { Log.d("cancelEvent", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("cancelEvent", "Error deleting document", e) }

    }
    private fun makeStrFromType(type:PetOwnerType):String
    {
        return when(type) {
            PetOwnerType.dog -> "dog"
            PetOwnerType.cat -> "cat"
            PetOwnerType.both -> "cat and dog"
            PetOwnerType.none -> ""
        }
    }

    fun makeTypeFromStrType(type:String):PetOwnerType
    {
        return when(type) {
             "dog"->PetOwnerType.dog
             "cat"->PetOwnerType.cat
              "cat and dog"->PetOwnerType.both
              ""->PetOwnerType.none
            else ->PetOwnerType.none
        }
    }
    fun petOwnerMakeStrFromType(type:BarberType):String
    {
        return when(type) {
            BarberType.dogBarber -> "dog barber"
            BarberType.catBarber -> "cat barber"
            BarberType.catAndDogBarber -> "cat and dog barber"
            BarberType.none -> ""
        }
    }

    fun petOwnerMakeTypeFromStrType(type:String):BarberType
    {
        return when(type) {
            "dog barber"->BarberType.dogBarber
            "cat barber"->BarberType.catBarber
            "cat and dog barber"->BarberType.catAndDogBarber
            ""->BarberType.none
            else ->BarberType.none
        }
    }

    fun createEventAppointment(barber: Barber, petOwner: PetOwner, time: String, day: Int, month: Int, year: Int) {
        val eventBarber:AppointmentBarber = AppointmentBarber.Builder()
            .time(time)
            .phone(petOwner.phoneNumber)
            .type(petOwner.animalType)
            .namePet(petOwner.petName)
            .nameOwner(petOwner.name)
            .uidFireBasePetOwner(petOwner.uidFireBase)
            .infoAboutThePet(petOwner.moreDetails)
            .builder()
        val eventPetOwner:AppointmentPetOwner = AppointmentPetOwner.Builder()
            .time(time)
            .nameBarber(barber.name)
            .uidFireBaseBarber(barber.uidFireBase)
            .type(barber.type)
            .builder()
        createEventInPetBarberCalendar(barber.uidFireBase , year = year , month = month ,day = day, eventBarber)
        createEventInPetOwnerCalendar(petOwner.uidFireBase ,year = year , month = month ,day = day, eventPetOwner)
    }


}