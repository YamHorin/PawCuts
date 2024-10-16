package com.example.pawcuts.interfaces

import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

interface CallBackGeocodeListener {
    fun onGeocodeSuccess(location: LatLng)
    fun onGeocodeFailure(exception: Exception)
}