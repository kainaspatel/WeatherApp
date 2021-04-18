package com.myapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.myapplication.repository.data_class.WeatherInfoResponse
import com.myapplication.retrofit.ApiRequest
import com.myapplication.retrofit.RetrofitRequest


class CityRepository() {

    private val TAG = CityRepository::class.java.simpleName
    private var apiRequest: ApiRequest? = null
    val usersSuccessLiveData = MutableLiveData<WeatherInfoResponse>()
    val usersFailureLiveData = MutableLiveData<Boolean>()

    fun initRetrofit() {
        apiRequest = RetrofitRequest.getRetrofitInstance()!!.create(ApiRequest::class.java)
    }

    suspend fun getWeatherDetails(lat: String?,lon: String?) {

            val response = apiRequest!!.getWeatherDetails(lat, lon)
            if (response != null) {
                Log.e(TAG, "SUCCESS")
                Log.e(TAG, "${response!!.body()}")
                usersSuccessLiveData.postValue(response.body())
            } else {
                Log.e(TAG, "FAILURE")
                Log.e(TAG, "${response!!.body()}")
                usersFailureLiveData.postValue(true)
            }

    }



}