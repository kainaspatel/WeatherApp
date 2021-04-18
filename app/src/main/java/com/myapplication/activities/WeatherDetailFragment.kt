package com.myapplication.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myapplication.R
import com.myapplication.repository.data_class.WeatherData
import com.myapplication.utils.kelvinToCelsius
import com.myapplication.utils.unixTimestampToDateTimeString
import com.myapplication.utils.unixTimestampToTimeString
import com.myapplication.viewmodel.CityViewModel

import kotlinx.android.synthetic.main.fragment_weather_detail.*
import kotlinx.android.synthetic.main.layout_input_part.*
import kotlinx.android.synthetic.main.layout_sunrise_sunset.*
import kotlinx.android.synthetic.main.layout_weather_additional_info.*
import kotlinx.android.synthetic.main.layout_weather_basic_info.*
import org.koin.android.ext.android.inject

class WeatherDetailFragment : Fragment() {
    val TAG = this.javaClass.simpleName
    private val cityViewModel: CityViewModel? by inject()
    lateinit var mArray: List<String>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // mWebview.loadUrl("https://www.javatpoint.com")
        output_group.visibility = View.VISIBLE
        tv_error_message.visibility = View.GONE
        activity?.title = "Weather Detail"


        var cityName = arguments?.getString("cityname")
        Log.e(TAG, "onViewCreated: ${cityName}")

        tv_city.text = cityName
        mArray = cityName!!.split(",")

        registerObservers()
        getWeatherDetails();

    }

    private fun getWeatherDetails() {

        cityViewModel!!.getWeatherDetails(mArray[0], mArray[1])
    }

    private fun registerObservers() {
        activity?.let {
            cityViewModel!!.usersSuccessLiveData.observe(it, Observer { weatherResponse ->
                //if it is not null then we will display all users
                weatherResponse?.let {
                    var data = it
                    val weatherData = WeatherData(
                        dateTime = data!!.dt.unixTimestampToDateTimeString(),
                        temperature = data.main.temp.kelvinToCelsius().toString(),
                        cityAndCountry = "${data.name}, ${data.sys.country}",
                        weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                        weatherConditionIconDescription = data.weather[0].description,
                        humidity = "${data.main.humidity}%",
                        pressure = "${data.main.pressure} mBar",
                        visibility = "${data.visibility / 1000.0} KM",
                        sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                        sunset = data.sys.sunset.unixTimestampToTimeString()
                    )
                    Log.e(TAG, "registerObservers: ${weatherData}")





                    tv_date_time?.text = weatherData.dateTime
                    tv_temperature?.text = weatherData.temperature
                    tv_city_country?.text = weatherData.cityAndCountry

                    tv_weather_condition?.text = weatherData.weatherConditionIconDescription

                    tv_humidity_value?.text = weatherData.humidity
                    tv_pressure_value?.text = weatherData.pressure
                    tv_visibility_value?.text = weatherData.visibility

                    tv_sunrise_time?.text = weatherData.sunrise
                    tv_sunset_time?.text = weatherData.sunset

//                    weatherData.weatherConditionIconUrl?.let {
//                        Glide.with(applicationContext())
//                            .load(weatherData.weatherConditionIconUrl)
//                            .into(iv_weather_condition)
//                    }

                }
            })
        }


        activity?.let {
            cityViewModel!!.usersFailureLiveData.observe(it, Observer { isFailed ->
                isFailed?.let {
                    Toast.makeText(activity, "Oops! something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })


        }

    }
}