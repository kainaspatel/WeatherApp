package com.myapplication.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapplication.CoreApplication.Companion.getPref
import com.myapplication.R
import com.myapplication.adapter.CityAdapter
import com.myapplication.utils.AppUtils.hasNetwork
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import java.lang.reflect.Type

class HomeFragment  : Fragment() ,View.OnClickListener {
    val TAG = this.javaClass.simpleName
    var navController: NavController? = null
    private val adapter: CityAdapter by inject()
    val gson = Gson()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(activity!!, R.id.my_nav_host_fragment)
        activity?.title = "Home"


        btnHelp.setOnClickListener(MainActivity2@this)
        btnMap.setOnClickListener(MainActivity2@this)
        setupRecyclerView();
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter

        val json = getPref().getString("Set", "")
        if (json!!.isEmpty()) {
            Toast.makeText(activity,"click on Map to add bookmark!",Toast.LENGTH_LONG).show();
        }
        else{
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            val arrPackageData: ArrayList<String> = gson.fromJson(json, type)
            for (data in arrPackageData) {
                //result.setText(data)
                Log.e(TAG, "data form pref : ${data}" )
            }
            arrPackageData?.let {
                adapter.setCities(it)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.btnHelp ->{
                if(activity?.let { hasNetwork(it) }!!){
                    navController!!.navigate(R.id.action_homeFragment_to_helpFragment);
                }
                else{
                    Toast.makeText(activity,"No Internet! Please turn on..",Toast.LENGTH_LONG).show();
                }
            }
            R.id.btnMap ->{
                if(activity?.let { hasNetwork(it) }!!){
                    navController!!.navigate(R.id.action_homeFragment_to_mapsFragment);
                }
                else{
                    Toast.makeText(activity,"No Internet! Please turn on..",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}