package com.example.pawcuts.models

import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.util.UUID
import kotlin.random.Random

object DataGenerator {
    val arrayLinks:Array<String>  = arrayOf("https://bloximages.newyork1.vip.townnews.com/theleadernews.com/content/tncms/assets/v3/editorial/2/8a/28ad4068-efe0-11eb-9006-8f8d599b1f3b/6101bafc9d205.image.jpg?resize=1200%2C801",
        "https://s3-media0.fl.yelpcdn.com/bphoto/yrPIdgMpSLOuZPmBJnLTMA/l.jpg",
        "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/041dc3ad-6925-4065-b8c2-095297eaee45/dcfakxx-63e6c2fd-d266-4e53-8940-91f762faed98.jpg/v1/fill/w_1024,h_1287,q_75,strp/barber_s_cat_by_melcasipit_dcfakxx-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzA0MWRjM2FkLTY5MjUtNDA2NS1iOGMyLTA5NTI5N2VhZWU0NVwvZGNmYWt4eC02M2U2YzJmZC1kMjY2LTRlNTMtODk0MC05MWY3NjJmYWVkOTguanBnIiwiaGVpZ2h0IjoiPD0xMjg3Iiwid2lkdGgiOiI8PTEwMjQifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6aW1hZ2Uud2F0ZXJtYXJrIl0sIndtayI6eyJwYXRoIjoiXC93bVwvMDQxZGMzYWQtNjkyNS00MDY1LWI4YzItMDk1Mjk3ZWFlZTQ1XC9tZWxjYXNpcGl0LTQucG5nIiwib3BhY2l0eSI6OTUsInByb3BvcnRpb25zIjowLjQ1LCJncmF2aXR5IjoiY2VudGVyIn19.JqlQJCbbHt4j2Ih0J3q315-Qg1G3rgfJ1guvAUA_mrE")

    fun generateBarberList():List<Barber>
    {
        val gson: Gson = Gson()
        var barbers = mutableListOf<Barber>()
            barbers.add(
                Barber.Builder()
                    .name("Jose Joshua")
                    .price(100)
                    .location(gson.toJson(LatLng(32.07771, 34.80155)))
                    .email("JoseJoshua@mail.com")
                    .phoneNumber("+9725262378777")
                    .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                    .type(BarberType.dogBarber)
                    .moreDetails("professional groomer who specializes in styling, trimming, and maintaining the fur of pets, ensuring they look and feel their best. They provide services like haircuts, baths, nail trimming, and overall grooming to keep pets clean and comfortable.")
                    .uidFireBase(UUID.randomUUID().toString())
                    .builder()
            )
        barbers.add(
            Barber.Builder()
                .name("TOM Joshua")
                .price(120)
                .location(gson.toJson(LatLng(32.0777087, 34.8015509)))
                .email("TOMTOM@mail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catBarber)
                .moreDetails("A cat barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Hagit Toby")
                .price(120)
                .location(gson.toJson(LatLng(32.052335, 34.6238683)))
                .email("Hagit@gmail.com")
                .phoneNumber("+9725612654577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catAndDogBarber)
                .moreDetails("A barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Jermaine Yahya")
                .price(125)
                .location(gson.toJson(LatLng(32.0567087, 34.8895509)))
                .email("TM@mail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catAndDogBarber)
                .moreDetails("A pet barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("Albert Eyal")
                .price(127)
                .location(gson.toJson(LatLng(32.07878, 34.8078683)))
                .email("Albert@gmail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catBarber)
                .moreDetails("A cat barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Albert Rozen")
                .price(127)
                .location(gson.toJson(LatLng(32.0787835, 34.8078683)))
                .email("Albert@walla.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("A dog barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("ido Rozen")
                .price(147)
                .location(gson.toJson(LatLng(32.0787835, 34.8078683)))
                .email("idoMail@walla.com")
                .phoneNumber("+9725264584577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("A dog barber ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("Kurt Eyb")
                .price(178)
                .location(gson.toJson(LatLng(32.08776, 34.88140)))
                .email("Kurt@walla.com")
                .phoneNumber("+972512345677")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catAndDogBarber)
                .moreDetails("A pet barber, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("Abu Mohamad")
                .price(100)
                .location(gson.toJson(LatLng(32.09642, 34.83283)))
                .email("Mohamad@mail.com")
                .phoneNumber("+9725262378777")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("professional groomer who specializes in styling, trimming, and maintaining the fur of pets, ensuring they look and feel their best. They provide services like haircuts, baths, nail trimming, and overall grooming to keep pets clean and comfortable.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Kim Possible")
                .price(120)
                .location(gson.toJson(LatLng(32.071235, 34.11113)))
                .email("Kim@mail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catBarber)
                .moreDetails("A cat barber .")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Hagit Toby")
                .price(120)
                .location(gson.toJson(LatLng(32.0255, 34.444483)))
                .email("Hagit@gmail.com")
                .phoneNumber("+9725612654577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catAndDogBarber)
                .moreDetails("A barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Jermaine Yahya")
                .price(125)
                .location(gson.toJson(LatLng(32.0567087, 34.8895509)))
                .email("TM@mail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catBarber)
                .moreDetails("A pet barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("Albert Eyal")
                .price(127)
                .location(gson.toJson(LatLng(32.07455, 34.80455483)))
                .email("Albert@gmail.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.catBarber)
                .moreDetails("A cat barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        barbers.add(
            Barber.Builder()
                .name("Albert Rozen")
                .price(127)
                .location(gson.toJson(LatLng(32.071235, 34.8078683)))
                .email("Albert@walla.com")
                .phoneNumber("+9725612364577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("A dog barber is a grooming expert who specializes in trimming and maintaining the coats of cats, ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("ido Rozen")
                .price(147)
                .location(gson.toJson(LatLng(32.0787835, 34.8078683)))
                .email("idoMail@walla.com")
                .phoneNumber("+9725264584577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("A dog barber ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )

        barbers.add(
            Barber.Builder()
                .name("ido Rozen")
                .price(147)
                .location(gson.toJson(LatLng(32.0787835, 34.8078683)))
                .email("idoMail@walla.com")
                .phoneNumber("+9725264584577")
                .profilePhoto(arrayLinks[Random.nextInt(0,2)])
                .uidFireBase(UUID.randomUUID().toString())
                .type(BarberType.dogBarber)
                .moreDetails("A dog barber ensuring they remain clean and tangle-free. They offer services like fur styling, bathing, and nail trimming to keep cats comfortable and looking their best.")
                .builder()
        )
        return barbers

    }
    fun generateReviews(uid:String) {
        val reviews = mutableListOf<Review>()
        val dataManger = DataUsersManager.getInstance()
        for (j in 0..5)
        {
            for (i in 0..4) {
                val review = Review.Builder()
                        .name("doggy max $i")
                        .reviewTxt(
                            "\"I was so impressed with the pet barber at [Barber's Name]! My dog, " +
                                    "[Dog's Name], is usually a bit anxious at the groomer, but the barber " +
                                    "was incredibly patient and gentle with him. He did a fantastic job with" +
                                    " the grooming, and my dog looked and smelled amazing afterwards." +
                                    " I'll definitely be bringing him back in the future!\""
                        )
                        .rating((i + 1).toFloat())
                        .builder()
                reviews.add(review)
                //put reviews in the database
                dataManger.putNewReviewForBarber(uid , review)

            }

        }

    }


}