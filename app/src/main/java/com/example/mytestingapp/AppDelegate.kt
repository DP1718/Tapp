package com.example.mytestingapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppDelegate : Application() {

     companion object{
        lateinit var instance: AppDelegate
     }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }




}