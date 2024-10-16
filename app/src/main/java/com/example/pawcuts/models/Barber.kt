package com.example.pawcuts.models

import com.google.android.gms.maps.model.LatLng
data class Barber (
    val name :String? = "" ,
    val email :String? =""  ,
    val price:Int? = 0,
    val phoneNumber:String? = "" ,
    var profilePhoto:String? =""  ,
    val uidFireBase:String? = "" ,
    val moreDetails:String? =""  ,
    val location : LatLng? = LatLng(0.0,0.0) ,
    val type: BarberType? =BarberType.none
)
{

    class Builder (
        var name :String =  "",
        var email :String =  "",
        var price:Int = 0,
        var phoneNumber:String =  "",
        var profilePhoto:String = "",
        var uidFireBase:String ="",
        var moreDetails:String = "",
        var location : LatLng =LatLng(0.0, 0.0),
        var type: BarberType = BarberType.none
    )
    {

    fun name (name : String) = apply { this. name  = name}
    fun email (email : String) = apply { this. email  = email}
    fun price(price:Int) =apply { this.price = price }
    fun phoneNumber (phoneNumber : String) = apply { this. phoneNumber  = phoneNumber}
    fun profilePhoto (profilePhoto : String) = apply { this.profilePhoto  = profilePhoto }
    fun uidFireBase (uidFireBase : String) = apply { this. uidFireBase  = uidFireBase}
    fun moreDetails (moreDetails : String) = apply { this.moreDetails  = moreDetails }
    fun location (location : LatLng) = apply { this.location  = location }
    fun type (type : BarberType) = apply { this.type  = type }
    fun builder() = Barber(
                name,
                email,
                price,
                phoneNumber,
                profilePhoto,
                uidFireBase,
                moreDetails,
                location,
                type,
    )
    }


}