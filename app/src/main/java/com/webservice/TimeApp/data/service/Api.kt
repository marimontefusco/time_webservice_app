package com.webservice.TimeApp.data.service

import com.webservice.TimeApp.data.model.Main
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    //função mapearTempo
    @GET("weather")
    fun weatherMapping(@Query("q") cityName: String,
                       @Query("appid") api_key: String
    ): Call<Main> //chama a model Main
}

//https://api.openweathermap.org/data/2.5/weather?=q{city name}&appid={API key}
