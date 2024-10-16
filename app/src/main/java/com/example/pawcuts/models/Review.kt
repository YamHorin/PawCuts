package com.example.pawcuts.models

data class Review private constructor(
    val name:String,
    val rating:Float,
    val reviewTxt:String
)
{
    class Builder(
        var name:String,
        var rating:Float,
        var reviewTxt:String
    )
    {
        fun name (name: String) =apply {this.name = name}
        fun rating (rating: Float) =apply {this.rating = rating}
        fun reviewTxt (review: String) =apply {this.reviewTxt = review}
        fun builder() =  Review(name , rating , reviewTxt)

    }

}
