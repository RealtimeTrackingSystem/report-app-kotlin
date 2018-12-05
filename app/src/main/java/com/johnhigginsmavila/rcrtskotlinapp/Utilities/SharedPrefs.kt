package com.johnhigginsmavila.rcrtskotlinapp.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.johnhigginsmavila.rcrtskotlinapp.Model.User

class SharedPrefs(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USER_DATA = "userData"
    val REPORTER_ID = "reporterId"
    val USER_HOST = "userHost"
    val FIREBASE_TOKEN = "firebaseToken"

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userData: String
        get() = prefs.getString(USER_DATA, "")
        set(value) = prefs.edit().putString(USER_DATA, value).apply()

    var reporterId: String
        get() = prefs.getString(REPORTER_ID, "")
        set(value) = prefs.edit().putString(REPORTER_ID, value).apply()

    var userHost: String
        get() = prefs.getString(USER_HOST, "")
        set(value) = prefs.edit().putString(USER_HOST, value).apply()

    var firebaseToken: String
        get() = prefs.getString(FIREBASE_TOKEN, "")
        set(value) = prefs.edit().putString(FIREBASE_TOKEN, value).apply()

    val requestQueue: RequestQueue = Volley.newRequestQueue(context)


    val context = context
}