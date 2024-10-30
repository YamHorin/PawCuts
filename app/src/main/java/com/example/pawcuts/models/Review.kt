package com.example.pawcuts.models

data class Review private constructor(
    var name:String,
    val rating:Float,
    val reviewTxt:String,
    var isCollapsed: Boolean = true


)
{
    //empty constructor
    constructor() : this(
        name ="",
        reviewTxt = "",
        rating = 0.0F
    )
    fun toggleCollapse() = apply { this.isCollapsed = !isCollapsed }
    class Builder(
        var name:String ="",
        var rating:Float = 0.0F,
        var reviewTxt:String =""
    )
    {
        fun name (name: String) =apply {this.name = name}
        fun rating (rating: Float) =apply {this.rating = rating}
        fun reviewTxt (review: String) =apply {this.reviewTxt = review}
        fun builder() =  Review(name , rating , reviewTxt)

    }

}
