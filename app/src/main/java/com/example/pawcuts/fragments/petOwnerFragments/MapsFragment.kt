package com.example.pawcuts.fragments.petOwnerFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pawcuts.R
import com.example.pawcuts.models.Barber
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

//TODO: add google maps
class MapsFragment : Fragment() {
    private var listBarbers: List<Barber> = emptyList()
    private lateinit var googleMapObj: GoogleMap
    private val gson:Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_maps, container, false)
        val dataManager:DataUsersManager = DataUsersManager.getInstance()
        val barberRef = dataManager.referenceListBarbers
        val currentLocation =gson.fromJson(arguments?.getString("location"),LatLng::class.java)
        barberRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                if (value!=null)
                {
                    val listBarbersData = mutableListOf<Barber>()
                    for (child in snapshot.children)
                        child.getValue(Barber::class.java)?.let { listBarbersData.add(it) }
                    listBarbers = listBarbersData
                }
                else
                {
                    listBarbers = emptyList()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Data Error", "Failed to read value.", error.toException())            }

        })
        val supportMapFragment =
            (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
            supportMapFragment!!.getMapAsync { googleMap ->
                googleMapObj = googleMap
                for (barber in listBarbers) {
                    googleMap.addMarker(MarkerOptions().position(gson.fromJson(barber.location ,LatLng::class.java)).title(barber.name))
                }
                moveCameraMap(currentLocation)
        }
        return view

    }

    private fun moveCameraMap(location: LatLng)
    {
        googleMapObj.clear()
        googleMapObj.addMarker(MarkerOptions().position(location).title("user location"))
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(15f)
            .build()
        googleMapObj.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {
        fun init(location: String): MapsFragment {
            val fragment = MapsFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("location",location)
            fragment.arguments = args
            return fragment

        }
    }

}