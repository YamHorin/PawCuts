package com.example.pawcuts.fragments.appointmentFragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pawcuts.R
import com.example.pawcuts.databinding.FragmentBarberCardBinding
import com.example.pawcuts.interfaces.CallBackBackButtonPress
import com.example.pawcuts.interfaces.CallBackFireStoreResultBarber
import com.example.pawcuts.interfaces.CallBackShowReviewsAndAppointments
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import java.time.LocalDate
import java.util.Calendar


class BarberCardFragment : Fragment() {
    private lateinit var binding:FragmentBarberCardBinding
    private lateinit var barber: Barber
    private val gson:Gson = Gson()
    private lateinit var uidPetOwner: String
    private var dataUsersManager: DataUsersManager = DataUsersManager.getInstance()
    private var showReviews:Boolean = true
    private var isFavorite:Boolean = false
    var rating:Float = 0.0F
    var callBackBack :CallBackBackButtonPress? = null
    var callBackShowReviewsAndAppointments: CallBackShowReviewsAndAppointments? =null
    companion object{
        fun init(barber: Barber  , uidPetOwner:String): BarberCardFragment {
            val fragment = BarberCardFragment()
            val args = Bundle()
            val gson: Gson = Gson()
            args.putString("uid" , uidPetOwner)
            args.putString("Barber",gson.toJson(barber) )
            fragment.arguments = args
            return fragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarberCardBinding.inflate(inflater, container, false)
        binding.BarberCardFragmentFABGoBack.setOnClickListener({callBackBack?.goBack()})
        val barberStr =arguments?.getString("Barber")
        uidPetOwner = arguments?.getString("uid").toString()
        barber = gson.fromJson(barberStr , Barber::class.java)
        isBarberInFavorites()
        when(showReviews)
        {
            false ->binding.BarberCardFragmentFABMakeReview.text = buildString { append("see reviews") }

            true ->binding.BarberCardFragmentFABMakeReview.text = buildString { append("make a review") }
        }
        binding.BarberCardFragmentFABMakeReview.setOnClickListener({
            showReviews = !showReviews
            when(showReviews)
            {
                false ->binding.BarberCardFragmentFABMakeReview.text = buildString { append("see reviews") }

                true ->binding.BarberCardFragmentFABMakeReview.text = buildString { append("make a review") }
            }
            callBackShowReviewsAndAppointments?.reviews(showReviews)
        })
        binding.BarberCardFragmentFABMakeMeeting.setOnClickListener({callBackShowReviewsAndAppointments?.appointment()})
        when(isFavorite)
        {
            true -> binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item)
            false -> binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item_empty)
        }
        with(barber)
        {
                binding.barberItemFragmentMTVName.text = buildString {
                    append(name)
                    append("\n")
                    when(type)
                    {
                        BarberType.catBarber -> append("a cat barber")
                        BarberType.dogBarber -> append("a dog barber")
                        BarberType.catAndDogBarber ->  append("a cat and dog barber")
                        BarberType.none -> append("")
                    }

                }
            if(isTextTooBig(binding.barberItemFragmentMTVInfo , moreDetails))
                binding.barberItemFragmentMTVInfo.ellipsize = TextUtils.TruncateAt.END

                binding.barberItemFragmentIMGFavorite.setOnClickListener({
                isFavorite = !isFavorite
                when(isFavorite)
                {
                    true -> {
                    binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item)
                    dataUsersManager.addBarberIntoFavoriteList(uidPetOwner ,this)
                    }
                    false -> {
                        binding.barberItemFragmentIMGFavorite.setImageResource(R.drawable.heart_item_empty)
                        dataUsersManager.deleteBarberFromFavoriteList(uidPetOwner , this)
                    }
                }


            })
            binding.barberItemFragmentMTVPrice.text = buildString {
                append(price)
                append("$")}
            binding.barberItemFragmentMTVPhoneNumber.text = phoneNumber
            Glide.with(binding.root.context).load(profilePhoto).centerCrop().placeholder(R.drawable.logo).into(binding.barberItemFragmentIMGPoster)
            getClosetsEvent(uidFireBase , binding.barberItemFragmentMTVAvailability)
            binding.barberItemFragmentRTNGRating.rating = barber.ratingScore
            binding.barberItemFragmentMTVInfo.text =moreDetails
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun getClosetsEvent(
        uidFireBase: String,
        barberItemFragmentMTVAvailability: MaterialTextView
    ) {
        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)+1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        barberItemFragmentMTVAvailability.text = "$day.$month.$year 8:00-9:00"
        val listHours = mutableListOf<String>()
        for (i in 8..20)
            listHours.add("$i:00-${i+1}:00")

        val calendarManager = CalendarManager.getInstance()
        calendarManager.callBackFireStoreResultBarber = object : CallBackFireStoreResultBarber
        {
            override fun noEventsForUser() {
                barberItemFragmentMTVAvailability.text = "$day.$month.$year 8:00-9:00"
            }

            override fun userPetBarberHasEvents(result: QuerySnapshot) {
                val list: MutableList<String> = mutableListOf()
                for (document in result) {
                    val time:String = document.getString("time").toString()
                    listHours.remove(time)
                }
                for (i in 8..20)
                {
                    if (!listHours.contains("$i:00-${i+1}:00"))
                    {
                        barberItemFragmentMTVAvailability.text = "$day.$month.$year $i:00-${i+1}:00"
                        break
                    }
                }
            }
        }

        calendarManager.getEventsFromCalendarBarber(uidFireBase  ,year , month , day)

    }
    fun incrementDate(year: Int, month: Int, day: Int): IntArray {
        val date = LocalDate.of(year, month, day)
        val nextDate = date.plusDays(1)
        return intArrayOf(nextDate.year, nextDate.monthValue, nextDate.dayOfMonth)
    }

    private fun isTextTooBig(textView: MaterialTextView, text: String): Boolean {
        val layout = textView.layout
        if (layout != null) {
            val textWidth = layout.getLineWidth(0) // Get width of the first line of text
            val availableWidth = textView.width - textView.paddingLeft - textView.paddingRight
            return textWidth > availableWidth
        }
        return false
    }

    private fun isBarberInFavorites()
    {
        val listRef  = dataUsersManager.getListFavoriteUser(uidPetOwner)
        listRef.get().addOnSuccessListener { result->
            if (result!=null)
            {
                for (b in result.children)
                {
                    val barberList = b.getValue(Barber::class.java)
                    if (barber.uidFireBase==barberList?.uidFireBase)
                        isFavorite=true
                }
                isFavorite =false

            }
        }
    }

}