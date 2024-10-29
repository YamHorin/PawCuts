package com.example.pawcuts.models

import com.google.android.gms.maps.model.LatLng
import kotlin.math.cos
import kotlin.math.sin

class Barber (
    val name :String,
    val email :String,
    val price:Int,
    val phoneNumber:String,
    var profilePhoto:String,
    val uidFireBase:String,
    val moreDetails:String,
    val location : String,
    val type: BarberType,
    var ratingScore:Float
)
{
    fun setRating(reviews: List<Review>)
    {
        var sum:Float = 0.0F
        for(review in reviews)
        {
            sum+=review.rating
        }
        this.ratingScore = sum/reviews.size
    }

    //empty c'tor:
    constructor() : this(
        name = "",
        email = "",
        price = 0,
        phoneNumber = "",
        profilePhoto ="",
        uidFireBase = "",
        moreDetails = "",
        location = "",
        type = BarberType.none,
        ratingScore= 0.0F
    )
    class Builder (
        var name :String =  "",
        var email :String =  "",
        var price:Int = 0,
        var phoneNumber:String =  "",
        var profilePhoto:String = "",
        var uidFireBase:String ="",
        var moreDetails:String = "",
        var location : String ="LatLng(0.0, 0.0)",
        var type: BarberType = BarberType.none,
        var ratingScore: Float = 0.0F
    )
    {

    fun name (name : String) = apply { this. name  = name}
    fun email (email : String) = apply { this. email  = email}
    fun price(price:Int) =apply { this.price = price }
    fun phoneNumber (phoneNumber : String) = apply { this. phoneNumber  = phoneNumber}
    fun profilePhoto (profilePhoto : String) = apply { this.profilePhoto  = profilePhoto }
    fun uidFireBase (uidFireBase : String) = apply { this. uidFireBase  = uidFireBase}
    fun moreDetails (moreDetails : String) = apply { this.moreDetails  = moreDetails }
    fun location (location : String) = apply { this.location  = location }
    fun type (type : BarberType) = apply { this.type  = type }
    fun ratingScore( ratingScore: Float) = apply { this.ratingScore = ratingScore }
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
                 ratingScore
    )
    }

    companion object{
        fun calculateDistance(locationUser: LatLng, locationBarber  : LatLng): Int {
                    val R = 6371; // Radius of the earth in km
                    val lat1 = locationUser.latitude
                    val lat2 = locationBarber.latitude
                    val lon2 = locationUser.longitude
                    val lon1 = locationBarber.longitude
                    val dLat = deg2rad(lat2-lat1);  // deg2rad below
                    val dLon = deg2rad(lon2-lon1);
                    val a = sin(dLat/2) * sin(dLat/2) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                                sin(dLon/2) * sin(dLon/2)
                    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                    val d = R * c; // Distance in km
                    return d.toInt();
                }

                fun deg2rad(deg: Double): Double {
                    return deg * (Math.PI/180)
                }

                fun getRatingScore(reviews: List<Review>): Float {
                    var sum:Float = 0.0F
                    for(review in reviews)
                    {
                        sum+=review.rating
                    }
                    return sum/reviews.size
                }

    }

}



