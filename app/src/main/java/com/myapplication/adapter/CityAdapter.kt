package com.myapplication.adapter


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

import com.google.gson.Gson
import com.myapplication.CoreApplication
import com.myapplication.R
import com.myapplication.utils.AppUtils.hasNetwork

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityHolder>() {
    private var mCities: MutableList<String> = ArrayList()
    var navController: NavController? = null
    val gson = Gson()
    lateinit var mContext : Context ;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)
         mContext = parent.context ;
        navController =
            Navigation.findNavController(parent.context as Activity, R.id.my_nav_host_fragment)

        return CityHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val currentCity = mCities[position]
        holder.textViewDescription.text = currentCity
        holder.setListeners(currentCity ,position)
        holder.ivRowDelete.setOnClickListener(View.OnClickListener {
            mCities.removeAt(position)
            val json = CoreApplication.getPref().getString("Set", "")
            val editor = CoreApplication.getPref().edit()
            editor.putString("Set", json)
            editor.commit()
            val json2 = gson.toJson(mCities)
            editor.putString("Set", json2)
            editor.commit()
            notifyDataSetChanged()
        })

    }

    override fun getItemCount(): Int {
        return mCities.size
    }

    fun setCities(mCities: MutableList<String> ) {
        this.mCities = mCities
        notifyDataSetChanged()
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setListeners(currentCity: String , position : Int) {

            ivRowEdit.setOnClickListener {
                if(hasNetwork(mContext)){
                    var bundle = bundleOf("cityname" to currentCity)
                    navController!!.navigate(R.id.action_homeFragment_to_weatherDetailFragment, bundle);                }
                else{
                    Toast.makeText(mContext,"No Internet! Please turn on..",Toast.LENGTH_LONG).show();
                }
            }
        }

        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        var ivRowDelete: ImageView = itemView.findViewById(R.id.ivRowDelete)
        var ivRowEdit: ImageView = itemView.findViewById(R.id.ivRowEdit)

    }
}