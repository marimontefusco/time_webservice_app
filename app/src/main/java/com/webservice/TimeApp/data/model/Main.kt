package com.webservice.TimeApp.data.model

import com.google.gson.JsonObject

data class Main (
    val main: JsonObject,
    val sys: JsonObject,
    val weather: List<WeatherResponse>,
    val name: String
)
