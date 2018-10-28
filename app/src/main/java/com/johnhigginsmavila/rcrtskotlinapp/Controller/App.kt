package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.app.Application
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}