package com.example.pawcuts.utilities

import android.content.Context
import android.util.Log
import com.example.pawcuts.interfaces.CallBackDataUserFound
import com.example.pawcuts.interfaces.CallBackUserTypeFound
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.models.DataGenerator
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.example.pawcuts.models.Review
import com.example.pawcuts.models.UserType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.ref.WeakReference

class DataUsersManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)
    val database = Firebase.database
    var callBackDataUserFound: CallBackDataUserFound? = null
    var callBackUserTypeFound: CallBackUserTypeFound? =null
    val referenceListBarbers: DatabaseReference = database.getReference("Barbers")

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

    fun writeNewUserPetOwnerDataBase(petOwner: PetOwner): DatabaseReference {
        val ref: DatabaseReference = database.getReference(petOwner.uidFireBase)
        ref.setValue(petOwner)
        return ref
    }

    fun writeNewUserPetBarberDataBase(barber: Barber) {
        //gets the list and see the number
        referenceListBarbers.child(barber.uidFireBase).setValue(barber)
    }


    //to read a object from database
    //1. make connection
    //2. getUserDataJson
    fun makeConnectionDatabaseRef(uidUser: String, userType: UserType): DatabaseReference {

        if (userType == UserType.petBarber)
            return referenceListBarbers.child(uidUser)
        return database.getReference(uidUser)

    }

    fun makeRefConnectionBarber(uidFireBase: String) {
        database.getReference("Barbers/$uidFireBase")
    }

    fun makeRefConnectionPetOwner(uidFireBase: String) {
        database.getReference(uidFireBase)
    }





    fun updateMoreDetailsUser(uidUser: String, userType: UserType, moreDetailsText: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("moreDetails").setValue(moreDetailsText)
    }

    fun updateNameUser(uidUser: String, userType: UserType, nameText: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("name").setValue(nameText)

    }

    fun updateEmailUser(uidUser: String, userType: UserType, newEmailText: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("email").setValue(newEmailText)

    }

    fun updateLocationUser(uidUser: String, userType: UserType, location: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("location").setValue(location)

    }

    fun updatePhoneUser(uidUser: String, userType: UserType, phoneTextNew: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("phoneNumber").setValue(phoneTextNew)

    }

    fun updateProfilePhotoUser(uidUser: String, userType: UserType, profilePhotoUrl: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("profilePhoto").setValue(profilePhotoUrl)

    }

    fun updateAnimalNameUser(uidUser: String, userType: UserType, animalNameText: String) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("petName").setValue(animalNameText)

    }

    fun updatePriceUser(uidUser: String, userType: UserType, priceText: Int) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("price").setValue(priceText)

    }

    fun updateAnimalTypeUser(uidUser: String, userType: UserType, animalTypeText: PetOwnerType) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("animalType").setValue(animalTypeText)
    }

    fun updateBarberTypeUser(uidUser: String, userType: UserType, typeText: BarberType) {
        val ref = makeConnectionDatabaseRef(uidUser, userType)
        ref.child("type").setValue(typeText)
    }

    fun getBarberReviews(uidFireBase: String): DatabaseReference {
        return referenceListBarbers.child(uidFireBase).child("reviews")
    }

    fun getUserType(uidFireBase: String)
    {
        referenceListBarbers.get().addOnSuccessListener {
            var isInTheList:Boolean = false
            for (child in it.children)
                if (child.getValue(Barber::class.java)?.uidFireBase==uidFireBase)
                    isInTheList = true

            when(isInTheList)
            {
                true ->  callBackUserTypeFound?.userBarber()
                false -> callBackUserTypeFound?.userPetOwner()
            }

        }.addOnFailureListener{
            Log.d("getUserType" , "Error getting the list ")
        }
    }

    fun getListFavoriteUser(uidFireBase: String): DatabaseReference {
        Log.d("Favorites" , "get the list ref of uid :$uidFireBase")
        return database.getReference("Favorites of PetOwner: $uidFireBase")
    }
    fun deleteBarberFromFavoriteList(uidFireBase: String, barber: Barber)
    {
        Log.d("Favorites" , "delete barber $barber from list of uid $uidFireBase")
        database.getReference("Favorites of PetOwner: $uidFireBase").child(barber.uidFireBase).removeValue()
    }
    fun addBarberIntoFavoriteList(uidFireBase: String, barber: Barber) {
        Log.d("Favorites" , "added barber $barber into list of uid $uidFireBase")
        database.getReference("Favorites of PetOwner: $uidFireBase").child(barber.uidFireBase).setValue(barber)
    }
    fun makeListBarbers()
    {
        referenceListBarbers.setValue(DataGenerator.generateBarberList())
    }
    fun getUserDataBarber(uidUser: String)
    {
        val referenceUser = makeConnectionDatabaseRef(uidUser, UserType.petBarber)
        referenceUser.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Barber::class.java)
                if (value != null) {
                    Log.d("dataSnapshot", value.toString())
                    callBackDataUserFound?.dataIsReady(value)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Data Error", "Failed to read value.", error.toException())
                callBackDataUserFound?.exceptionData()
            }
        })

    }

    fun getUserDataPetOwner(uidUser: String)
    {
        val referenceUser = makeConnectionDatabaseRef(uidUser, UserType.petOwner)
        referenceUser.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(PetOwner::class.java)
                if (value != null) {
                    Log.d("dataSnapshot", value.toString())
                    callBackDataUserFound?.dataIsReady(value)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Data Error", "Failed to read value.", error.toException())
                callBackDataUserFound?.exceptionData()
            }
        })
    }

    fun putNewReviewForBarber(uidUser: String , review: Review)
    {
       database.getReference("reviews for uid:$uidUser").push().setValue(review)
    }
    fun getRefrenceForReviewsOfBarber(uidUser: String): DatabaseReference {
        return database.getReference("reviews for uid:$uidUser")
    }

    fun updateRatingScoreBarber(uid: String, list: MutableList<Review>) {
        referenceListBarbers.child(uid).child("ratingScore").setValue(Barber.getRatingScore(list))
    }



}