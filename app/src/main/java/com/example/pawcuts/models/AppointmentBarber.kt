package com.example.pawcuts.models

data class AppointmentBarber private constructor(
    val time:String,
    val namePet:String,
    val nameOwner:String,
    val infoAboutThePet:String,
    val phone:String,
    val uidFireBasePetOwner:String,
    val type: PetOwnerType,
    var isCollapsed: Boolean = true
)

{
    fun toggleCollapse() = apply { this.isCollapsed = !isCollapsed }

    class Builder(
        var time:String = "",
        var namePet:String = "",
        var nameOwner:String = "",
        var phone:String ="",
        var infoAboutThePet:String = "",
        var uidFireBasePetOwner:String ="",
        var type: PetOwnerType =PetOwnerType.none
    )
    {
        fun time(time:String) = apply { this.time = time}
        fun namePet(namePet:String) = apply { this.namePet = namePet}
        fun nameOwner(nameOwner:String) = apply { this.nameOwner = nameOwner}
        fun infoAboutThePet(infoAboutThePet:String) = apply { this.infoAboutThePet = infoAboutThePet}
        fun phone(phone:String) = apply { this.phone = phone}
        fun type(type:PetOwnerType) = apply { this.type = type}
        fun uidFireBasePetOwner(uidFireBasePetOwner:String) = apply { this.uidFireBasePetOwner = uidFireBasePetOwner}
        fun builder() = AppointmentBarber(time, namePet, nameOwner,infoAboutThePet,phone ,uidFireBasePetOwner, type)


    }
}
