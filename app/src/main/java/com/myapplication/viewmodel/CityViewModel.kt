package com.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.repository.CityRepository
import kotlinx.coroutines.launch


class CityViewModel(private var repository: CityRepository) : ViewModel() {

    val usersSuccessLiveData = repository.usersSuccessLiveData
    val usersFailureLiveData = repository.usersFailureLiveData

    fun initRetrofit() {
        repository!!.initRetrofit()
    }

    fun getWeatherDetails(lat: String?,lon: String?)  {
        initRetrofit()
        viewModelScope.launch {
            repository!!.getWeatherDetails(lat, lon)
        }
    }

}