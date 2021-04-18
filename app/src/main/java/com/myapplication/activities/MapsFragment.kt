package com.myapplication.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapplication.CoreApplication.Companion.getPref
import com.myapplication.R
import com.myapplication.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_maps.*
import java.lang.reflect.Type


class MapsFragment : Fragment(), View.OnClickListener {
    val TAG = this.javaClass.simpleName
    lateinit var location: LatLng;
    lateinit var favorites: ArrayList<String>;
    val gson = Gson()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(23.0225, 72.5714)
        location = LatLng(23.0225, 72.5714)

        googleMap.addMarker(MarkerOptions().position(sydney).title("Ahmedabad"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(23.0225, 72.5714), 12.0f))


        googleMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latlng: LatLng) {
                // Clears the previously touched position
                googleMap.clear();
                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

                location = LatLng(latlng.latitude, latlng.longitude)
                Log.e("TAG", "onMapClick: lat  " + latlng.latitude)
                Log.e("TAG", "onMapClick: long " + latlng.latitude)
                var marker = googleMap.addMarker(
                    MarkerOptions().position(location)
                        .title("" + latlng.latitude + "," + latlng.latitude)
                )
                marker.showInfoWindow()

            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        activity?.title = "Map"

        favorites = ArrayList<String>()

        mapFragment?.getMapAsync(callback)
        btnAdd.setOnClickListener(this)
        btnRead.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnRead -> {
                readPref()
            }
            R.id.btnAdd -> {
                if (activity?.let { AppUtils.hasNetwork(it) }!!) {


                    if (location != null) {
                        Toast.makeText(activity, "Bookmark added!", Toast.LENGTH_LONG).show()
                        val json = getPref().getString("Set", "")
                        if (json.equals("")) {
                            favorites!!.add("" + location.latitude + "," + location.longitude)
                            val json = gson.toJson(favorites)
                            val editor = getPref().edit()
                            editor.putString("Set", json)
                            editor.commit()
                        } else {
                            var type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
                            var arrPackageData: ArrayList<String> = gson.fromJson(json, type)
                            arrPackageData?.let {
                                arrPackageData.add("" + location.latitude + "," + location.longitude)
                                val json = gson.toJson(arrPackageData)
                                val editor = getPref().edit()
                                editor.putString("Set", json)
                                editor.commit()
                            }
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            "reverse geocode error : select city again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Toast.makeText(activity, "No Internet! Please turn on..", Toast.LENGTH_LONG)
                        .show();
                }
            }
        }
    }

    private fun readPref() {
        val json = getPref().getString("Set", "")

        if (json!!.isEmpty()) {
            Toast.makeText(activity, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            val arrPackageData: ArrayList<String> = gson.fromJson(json, type)
            for (data in arrPackageData) {
                //result.setText(data)
                Log.e(TAG, "data form pref : ${data}")
            }
        }
    }

}