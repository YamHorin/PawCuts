package com.example.pawcuts.models

data class AppointmentPetOwner private constructor(
    val time:String,
    val nameBarber:String,
    val uidFireBaseBarber:String,
    val type: BarberType
)
{
    class Builder(
        var time:String ="",
        var nameBarber:String ="",
        var uidFireBaseBarber:String ="",
        var type: BarberType  =BarberType.none
    )
    {
        fun time(time:String) = apply { this.time =time }
        fun nameBarber(nameBarber:String) = apply { this.nameBarber =nameBarber }
        fun uidFireBaseBarber(uidFireBaseBarber:String) = apply { this.uidFireBaseBarber =uidFireBaseBarber }
        fun type(type: BarberType) = apply { this.type =type }
        fun builder() = AppointmentPetOwner(time , nameBarber, uidFireBaseBarber ,type)


    }
}