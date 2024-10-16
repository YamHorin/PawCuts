package com.example.pawcuts.models

import com.google.android.gms.maps.model.LatLng

data class PetOwner private constructor(
    val name :String,
    val petName: String,
    val email :String,
    val phoneNumber:String,
    var profilePhoto:String,
    val moreDetails:String,
    val uidFireBase: String,
    var location : LatLng,
    val animalType: PetOwnerType
)

{

    fun profilePhoto(uploadImage: String) {
        this.profilePhoto = uploadImage
    }

    class Builder(
        var name: String  = "",
        var petName:String  = "",
        var email:String  = "",
        var phoneNumber:String = "",
        var profilePhoto:String ="",
        var moreDetails:String = "",
        var uidFireBase: String = "",
        var location : LatLng =LatLng(0.0, 0.0),
        var animalType:PetOwnerType  = PetOwnerType.none
    )
    {
        fun name (name :String)  = apply { this.name  = name }
        fun petName (petName :String)  = apply { this.petName  = petName }
        fun email(email :String)  = apply { this.email  = email }
        fun profilePhoto(profilePhoto :String)  = apply { this.profilePhoto  = profilePhoto }
        fun phoneNumber(phoneNumber :String)  = apply { this.phoneNumber  = phoneNumber }
        fun animalType(animalType :PetOwnerType)  = apply { this.animalType  = animalType }
        fun moreDetails (moreDetails : String) = apply { this.moreDetails  = moreDetails }
        fun uidFireBase (uidFireBase : String) = apply { this.uidFireBase  = uidFireBase }
        fun location (location:LatLng ) = apply {this.location = location  }
        fun builder()  =PetOwner(name,
                petName,
                email,
                phoneNumber,
                profilePhoto,
                moreDetails,
                uidFireBase,
                location,
                animalType)

    }
}
