package com.example.pawcuts.fragments.petOwnerFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.adapters.BarbersAdapter
import com.example.pawcuts.databinding.FragmentFindPetBarbersBinding
import com.example.pawcuts.interfaces.CallBackGoogleMapAndBarberCard
import com.example.pawcuts.interfaces.CallBackShowBarberCardAndFavoriteBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class FindPetBarbersFragment : Fragment() {
    private lateinit var binding:FragmentFindPetBarbersBinding
    private var dataUsersManager:DataUsersManager = DataUsersManager.getInstance()
    private var adapter: BarbersAdapter = BarbersAdapter()
    private lateinit var locationUser: LatLng
    private lateinit var uid:String
    private val gson: Gson = Gson()
    var callBackMap: CallBackGoogleMapAndBarberCard?= null
    companion object
    {
        fun init(locationUser:LatLng , uid:String):FindPetBarbersFragment{
            val fragment = FindPetBarbersFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("uid" , uid)
            args.putString("location",gson.toJson(locationUser) )
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationUser = gson.fromJson(arguments?.getString("location"),LatLng::class.java)
        uid = arguments?.getString("uid").toString()
        binding = FragmentFindPetBarbersBinding.inflate(inflater, container, false)
        binding.FindPetBarbersFragmentFABLocationMap.setOnClickListener({callBackMap?.show()})
        //making adapter
        adapter.loctionOfUser = locationUser

        adapter.callBackShowBarberCardAndFavoriteBarber  = object :CallBackShowBarberCardAndFavoriteBarber
        {
            override fun show(barber: Barber) {
                callBackMap?.BarberCard(barber)
            }

            override fun favorite(barber: Barber, position: Int) {
                //add Barber into favorite into the real time database
                //check if he is here
                if (adapter.isBarberInTheList(barber))
                {
                    dataUsersManager.deleteBarberFromFavoriteList(uid , barber)
                }
                //if is not add
                else{
                    dataUsersManager.addBarberIntoFavoriteList(uid , barber)
                }

            }

        }
        val listRef = dataUsersManager.referenceListBarbers
        listRef.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.value

                    if (value != null) {
                       //because the real time database wiil get the list in array list of map , we need to convert the objects into Barber
                        val list = mutableListOf<Barber>()
                        for (childSnapShot in dataSnapshot.children)
                            childSnapShot.getValue(Barber::class.java)?.let { list.add(it) }
                        adapter.barbers = list
                    }
                    else
                    {
                        adapter = BarbersAdapter(emptyList())
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("Data Error", "Failed to read value.", error.toException())
                }
            })

        val listFabRef= dataUsersManager.getListFavoriteUser(uid)
        listFabRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value

                if (value != null) {
                    //because the real time database will get the list in array list of map , we need to convert the objects into Barber
                    val list = mutableListOf<Barber>()
                    for (childSnapShot in dataSnapshot.children)
                        childSnapShot.getValue(Barber::class.java)?.let { list.add(it) }
                    Log.d("BarbersAdapter", "Favorites before update: ${adapter.favorites}")
                    adapter.favorites = list
                    Log.d("BarbersAdapter", "Favorites after update: ${adapter.favorites}")
                    adapter.notifyDataSetChanged()
                }
                else{
                        adapter.favorites = emptyList()
                        adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })

        binding.FindPetBarbersFragmentRV.adapter = adapter
            binding.FindPetBarbersFragmentRV.layoutManager = LinearLayoutManager(requireContext())
       return binding.root
    }

}