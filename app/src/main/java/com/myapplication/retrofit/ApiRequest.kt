package com.myapplication.retrofit

import com.myapplication.repository.data_class.WeatherInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {


    @GET("weather")
    suspend fun getWeatherDetails( @Query("lat") lat: String?,
                               @Query("lon") lon: String?
    ): Response<WeatherInfoResponse>
}