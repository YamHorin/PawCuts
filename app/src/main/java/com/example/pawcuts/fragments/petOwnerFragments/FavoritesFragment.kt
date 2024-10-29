package com.example.pawcuts.fragments.petOwnerFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.adapters.BarbersAdapter
import com.example.pawcuts.databinding.FragmentFavoritesBinding
import com.example.pawcuts.interfaces.CallBackGoogleMapAndBarberCard
import com.example.pawcuts.interfaces.CallBackShowBarberCardAndFavoriteBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private var dataUsersManager: DataUsersManager = DataUsersManager.getInstance()
    private lateinit var adapter: BarbersAdapter
    private lateinit var locationUser: LatLng
    private lateinit var uid: String
    private val gson: Gson = Gson()
    var callBackMap: CallBackGoogleMapAndBarberCard?= null
    companion object
    {
        fun init(locationUser: LatLng , uid:String):FavoritesFragment{
            val fragment = FavoritesFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("location",gson.toJson(locationUser) )
            args.putString("uid" , uid)
            fragment.arguments = args
            return fragment

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uid  = arguments?.getString("uid").toString()
        locationUser = gson.fromJson(arguments?.getString("location"),LatLng::class.java)
        binding = FragmentFavoritesBinding.inflate(inflater , container ,false)
        val listRef = dataUsersManager.getListFavoriteUser(uid)
        listRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value

                if (value != null) {
                    val list = mutableListOf<Barber>()
                    for (child in dataSnapshot.children)
                        child.getValue(Barber::class.java)?.let { list.add(it) }
                    adapter.barbers = list
                    adapter.updateFavorites(list)
                    adapter.notifyDataSetChanged()
                }
                else{
                    adapter.barbers = (emptyList())
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
        adapter = BarbersAdapter()

        adapter.callBackShowBarberCardAndFavoriteBarber  = object :CallBackShowBarberCardAndFavoriteBarber
        {
            override fun show(barber: Barber) {
                callBackMap?.BarberCard(barber)
            }

            override fun favorite(barber: Barber, barberItemFragmentIMGFavorite: Int) {
                //add Barber into favorite into the real time database
                //check if he is here
                if (adapter.favorites.contains(barber))
                {
                    val list: MutableList<Barber> = adapter.favorites.toMutableList()
                    list.remove(barber)
                    adapter.favorites =list
                    adapter.notifyDataSetChanged()
                    dataUsersManager.deleteBarberFromFavoriteList(uid , barber)
                }
                //if is not add
                else{
                    val list: MutableList<Barber> = adapter.favorites.toMutableList()
                    list.add(barber)
                    adapter.favorites =list
                    adapter.notifyDataSetChanged()
                    dataUsersManager.addBarberIntoFavoriteList(uid , barber)
                }

            }

        }
        adapter.loctionOfUser = locationUser
        binding.FindPetBarbersFragmentRV.adapter = adapter
        binding.FindPetBarbersFragmentRV.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }


}