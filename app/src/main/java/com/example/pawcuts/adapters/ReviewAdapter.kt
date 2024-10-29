package com.example.pawcuts.adapters

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcuts.databinding.ReviewItemBinding
import com.example.pawcuts.models.Review
import com.example.pawcuts.utilities.Constants
import java.util.function.Consumer
import kotlin.math.max

class ReviewAdapter(
    var reviews:List<Review> = emptyList()
): RecyclerView.Adapter<ReviewAdapter.EventViewHolder>()
{
    inner class EventViewHolder(val binding:ReviewItemBinding):
        RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return reviews.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        with(holder)
        {
            with(reviews.get(position))
            {
                binding.reviewMTVName.text = "$name:$reviewTxt"
                binding.reviewRTNGRating.rating = rating
                binding.reviewCVData.setOnClickListener {
                    val animatorSet = ArrayList<ObjectAnimator>()
                    if (isCollapsed)
                    {
                        animatorSet
                            .add(
                                ObjectAnimator
                                    .ofInt(
                                        binding.reviewMTVName,
                                        "maxLines",
                                        binding.reviewMTVName.lineCount
                                    ).setDuration((max((binding.reviewMTVName.lineCount - Constants.Animation.ACTORS_LIST_MIN_LINE).toDouble(), 0.0) * 50L).toLong()))

                    }
                    else
                    {
                        animatorSet
                            .add(
                                ObjectAnimator
                                    .ofInt(
                                        binding.reviewMTVName,
                                        "maxLines",
                                        Constants.Animation.OVWERVIEW_MIN_LINES
                                    ).setDuration((max((binding.reviewMTVName.lineCount - Constants.Animation.ACTORS_LIST_MIN_LINE).toDouble(), 0.0) * 50L).toLong()))

                    }
                    toggleCollapse()
                    animatorSet.forEach(Consumer { obj: ObjectAnimator -> obj.start() })
                }
            }
        }
    }
    fun getRatingScore(): Float {
        var sum:Float = 0.0F
        for(review in reviews)
            sum+=review.rating
        return sum/reviews.size
    }
}
