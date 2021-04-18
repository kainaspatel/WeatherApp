package com.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.myapplication.di.dbModule
import com.myapplication.di.repositoryModule
import com.myapplication.di.retrofitModule
import com.myapplication.di.uiModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoreApplication: Application() {



    init {
        instance = this
    }

    companion object {
        private var instance: CoreApplication? = null


        fun applicationContext() : Context {

            return instance!!.applicationContext
        }

        fun getPref() : SharedPreferences {
            return  instance!!.applicationContext!!.getSharedPreferences("USER", MODE_PRIVATE)
        }
    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule,  uiModule ,retrofitModule))
        }
    }
}