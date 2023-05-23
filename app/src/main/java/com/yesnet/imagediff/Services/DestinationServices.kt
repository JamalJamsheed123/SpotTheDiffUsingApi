package com.yesnet.imagediff.Services


import com.yesnet.imagediff.Models.GameModel
import com.yesnet.imagediff.Models.GameModelClass
import retrofit2.Call
import retrofit2.http.GET

interface DestinationServices {

    @GET("/Box.json")
   fun getDestinationList(): Call<GameModel>
}

