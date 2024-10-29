package com.example.pawcuts.fragments.appointmentFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pawcuts.databinding.FragmentMakeReviewBinding
import com.example.pawcuts.interfaces.CallBackMakeReview
import com.example.pawcuts.interfaces.InterfaceFragments
import com.example.pawcuts.models.Review


class MakeAReviewFragment : Fragment(),InterfaceFragments {
    private lateinit var binding:FragmentMakeReviewBinding
    var callBackMakeReview: CallBackMakeReview? = null
    var name:String =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentMakeReviewBinding.inflate(inflater, container, false)
        binding.makeReviewFragmentSave.setOnClickListener({callBackMakeReview?.insert()})
        return binding.root
    }
    fun getUserInput(): Review {
        return Review.Builder()
            .name(name)
            .reviewTxt(binding.makeReviewFragmentTIELTextReview.text.toString())
            .rating(binding.makeReviewFragmentTIETRating.text.toString().toFloat())
            .builder()
    }

    override fun checkUserInput() {
        if (binding.makeReviewFragmentTIETRating.text.toString() == "" ||binding.makeReviewFragmentTIELTextReview.text.toString()=="")
            throw IllegalArgumentException()
    }


}