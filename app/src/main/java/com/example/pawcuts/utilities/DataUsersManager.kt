package com.example.pawcuts.utilities

import android.content.Context
import android.util.Log
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.models.DataManager
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.example.pawcuts.models.UserType
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.lang.ref.WeakReference

class DataUsersManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)
    val database = Firebase.database
    private var gson:Gson = Gson()
    private val referenceListBarbers:DatabaseReference =database.getReference("Barbers")
    companion object {
        @Volatile
        private var instance: DataUsersManager? = null

        fun init(context: Context): DataUsersManager {
            return instance ?: synchronized(this) {

                instance ?: DataUsersManager(context).also { instance = it }
            }
        }

        fun getInstance(): DataUsersManager {
            return instance
                ?: throw IllegalStateException("DataUsersManager must be initialized by calling init(context) before use.")
        }

    }
    fun initDBConnection(path: String): DatabaseReference {
        return database.getReference(path)
    }
    fun getListOfBarbers(): MutableList<Barber> {
        var listBarbers= mutableListOf<Barber>()
        referenceListBarbers.addListenerForSingleValueEvent(object :
            ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<List<Barber>>()

                if (value != null) {
                    listBarbers = value as MutableList<Barber>
                }


            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return listBarbers
    }
    fun writeNewUserPetOwnerDataBase(petOwner: PetOwner):DatabaseReference
    {
        val ref:DatabaseReference = database.getReference(makeRefName(petOwner.uidFireBase))
        ref.setValue(petOwner)
        return ref
    }
    fun writeNewUserPetBarberDataBase(barber: Barber): Int {
        //gets the list and see the number
        var listBarbers = getListOfBarbers()
        val number:Int = listBarbers.size
        listBarbers.add(barber)
        referenceListBarbers.setValue(listBarbers)
        return number
    }
    fun createNewListBarbersValue()
    {
        referenceListBarbers.setValue(DataManager.generateBarberList())
    }

    //to read a object from database
    //1. make connection
    //2. getUserDataJson
    fun makeConnectionDatabaseRef(uidUser: String): DatabaseReference {
        val listBarbers = getListOfBarbers()
        //see if there is an email equal to email user in the list
        for (i in 0..<listBarbers.size)
            if(listBarbers[i].uidFireBase == uidUser)
                return referenceListBarbers.child("$i")

        return database.getReference(makeRefName(uidUser))

    }

    fun getUserDataJson(referenceUser: DatabaseReference): String {
        val gson:Gson =Gson()
        var valueData:String = ""
        referenceUser.addListenerForSingleValueEvent(object :
        ValueEventListener { // For realtime data fetching from DB
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.value

            if (value != null) {
                Log.d("dataSnapshot" ,gson.toJson(value) )
                valueData = gson.toJson(value)
            }


        }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        return valueData
    }
    private fun makeRefName(uidUser:String): String {
        return uidUser
    }
    fun updateMoreDetailsUser(uidUser: String , moreDetailsText:String)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("moreDetails").setValue(moreDetailsText)

    }

    fun updateNameUser(uidUser:String , nameText:String)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("name").setValue(nameText)

    }
    fun updateEmailUser(uidUser:String  , newEmailText:String)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("email").setValue(newEmailText)

    }
    fun updateLocationUser(uidUser:String  , location: LatLng)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("location").setValue(location)

    }
    fun updatePhoneUser(uidUser:String  , phoneTextNew:String)
    {
        var ref = makeConnectionDatabaseRef(uidUser)
        ref.child("phoneNumber").setValue(phoneTextNew)

    }
    fun updateProfilePhotoUser(uidUser:String  , profilePhotoUrl:String)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("profilePhoto").setValue(profilePhotoUrl)

    }
    fun updateAnimalNameUser(uidUser:String, animalNameText:String)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("petName").setValue(animalNameText)

    }
    fun updatePriceUser(uidUser:String , phoneText:String , priceText:Int)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("price").setValue(priceText)

    }
    fun updateAnimalTypeUser(uidUser:String  , animalTypeText: PetOwnerType)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("animalType").setValue(animalTypeText)
    }
    fun updateBarberTypeUser(uidUser:String , TypeText: BarberType)
    {
        val ref = makeConnectionDatabaseRef(uidUser)
        ref.child("type").setValue(TypeText)
    }




}