package com.example.pawcuts.models

data class ReviewList private constructor(
    val barber: Barber,
    val list: MutableList<Review>
)
{
    class Builder(
        var barber: Barber,
        var list: MutableList<Review>
    )
    {
        fun barber(barber: Barber) = apply { this.barber = barber }
        fun list(list: MutableList<Review>) = apply { this.list = list }
        fun builder()  = ReviewList(barber , list)

    }

}