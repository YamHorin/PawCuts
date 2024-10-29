package com.example.pawcuts.fragments.barberFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawcuts.adapters.ReviewAdapter
import com.example.pawcuts.databinding.FragmentReviewsBarberScreenBinding
import com.example.pawcuts.models.Review
import com.example.pawcuts.utilities.DataUsersManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ReviewsBarberScreenFragment : Fragment() {
    private lateinit var binding:FragmentReviewsBarberScreenBinding
    private var adapter: ReviewAdapter =ReviewAdapter()
    private var uidFireBase =""

    companion object {
        fun init(uidFireBase:String): ReviewsBarberScreenFragment {
            val fragment = ReviewsBarberScreenFragment()
            val args = Bundle()
            args.putString("uid", uidFireBase)
            fragment.arguments = args
            return fragment

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uidFireBase  = arguments?.getString("uid").toString()
        binding = FragmentReviewsBarberScreenBinding.inflate(inflater, container, false)
        binding.reviewsBarberScreenFragmentRV.adapter = adapter
        binding.reviewsBarberScreenFragmentRV.layoutManager = LinearLayoutManager(requireContext())

        //get reviews
        val data:DataUsersManager = DataUsersManager.getInstance()
        val reviewsRef = data.getRefrenceForReviewsOfBarber(uidFireBase)
        reviewsRef.addValueEventListener(object :
            ValueEventListener { // For realtime data fetching from DB
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value

                if (value != null) {
                    val list = mutableListOf<Review>()
                    for (childSnapshot in dataSnapshot.children) {
                        childSnapshot.getValue(Review::class.java)?.let { list.add(it) }
                    }
                    adapter.reviews = list
                    data.updateRatingScoreBarber(uidFireBase , list)
                    adapter.notifyDataSetChanged()
                }
                else
                    adapter = ReviewAdapter(emptyList())
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })

        return binding.root
    }


