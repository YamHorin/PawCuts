package com.example.pawcuts.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawcuts.R
import com.example.pawcuts.databinding.BarberItemBinding
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackShowBarberCardAndFavoriteBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import java.util.Calendar

class BarbersAdapter(
    var barbers:List<Barber> = emptyList<Barber>(),
    var favorites:List<Barber> = emptyList<Barber>()
): RecyclerView.Adapter<BarbersAdapter.CardViewHolder>() {
    var callBackShowBarberCardAndFavoriteBarber: CallBackShowBarberCardAndFavoriteBarber? =null
    var loctionOfUser:LatLng = LatLng(0.0,0.0)
    private val gson:Gson = Gson()
    private val calanderManager:CalendarManager = CalendarManager.getInstance()
    private val dataUsersManager:DataUsersManager = DataUsersManager.getInstance()
    private lateinit var textAppointment:String
    fun getBarber(position: Int) = barbers[position]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = BarberItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return barbers.size
    }
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        with(holder)
        {
            with(barbers[position])
            {
                binding.barberItemFragmentMTVName.text = name
                binding.barberItemFragmentMTVPrice.text = buildString {
                append(price)
                append("$")
                    }
                Log.d("onBindViewHolder" , "favorites = $favorites")
                Log.d("onBindViewHolder" , "barbers[position] = "+barbers[position].toString())

                if (isBarberInTheList(this))
                    binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item)
                else
                    binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item_empty)
                 getClosetsEvent(uidFireBase , binding.barberItemFragmentMTVAvailability)
                binding.barberItemFragmentRTNGRating.rating = ratingScore
                val distance = Barber.calculateDistance(loctionOfUser , gson.fromJson(location,LatLng::class.java))
                if (distance<1)
                    binding.barberItemFragmentMTVDistance.text =buildString{ append("~1 km") }
                else
                {
                    binding.barberItemFragmentMTVDistance.text =buildString{
                        append(distance)
                        append(" km")
                    }

                }

                Glide
                    .with(binding.root.context)
                    .load(profilePhoto)
                    .centerCrop()
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.barberItemFragmentIMGPoster)


            }


        }
    }
fun isBarberInTheList(barber: Barber): Boolean {
    for (barberObj in favorites)
        if (barberObj.uidFireBase == barber.uidFireBase)
            return true
    return false
}

    private fun getClosetsEvent(
        uidFireBase: String,
        barberItemFragmentMTVAvailability: MaterialTextView
    ) {
        val calendar = Calendar.getInstance()

        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)+1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val listHours = mutableListOf<String>()
        for (i in 8..20)
            listHours.add("$i:00-${i+1}:00")
        val sizeList =listHours.size
        calanderManager.callBackFireStoreResultBarber = object :CallBackFireStoreResultBarber
        {
            override fun noEventsForUser() {
                barberItemFragmentMTVAvailability.text =buildString { append( "$day.$month.$year 8:00-9:00")}
            }

            override fun userPetBarberHasEvents(result: QuerySnapshot) {
                val list: MutableList<String> = mutableListOf()
                var isFull = true
                for (document in result) {
                    val time:String = document.getString("time").toString()
                    list.add(time)
                }
                for (i in 8..20)
                {
                    if (!list.contains("$i:00-${i+1}:00"))
                    {
                        isFull = false
                        barberItemFragmentMTVAvailability.text = buildString { append("$day.$month.$year $i:00-${i+1}:00")}
                        return
                    }
                }
                barberItemFragmentMTVAvailability.text = buildString { append("not available for today") }
            }

        }
        calanderManager.getEventsFromCalendarBarber(uidFireBase  ,year , month , day)

    }
    fun updateFavorites(newFavorites: List<Barber>) {
        favorites = newFavorites
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class CardViewHolder(val binding:BarberItemBinding):
        RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.barberItemFragmentCVData.setOnClickListener({
                if (callBackShowBarberCardAndFavoriteBarber!=null)
                 callBackShowBarberCardAndFavoriteBarber!!.show(getBarber(adapterPosition))})
            binding.barberItemFragmentIMGFavorite
                .setOnClickListener({callBackShowBarberCardAndFavoriteBarber?.favorite(
                    getBarber(adapterPosition),
                    adapterPosition
                )})
        }
    }
}