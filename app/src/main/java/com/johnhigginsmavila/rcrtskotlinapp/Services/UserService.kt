package com.johnhigginsmavila.rcrtskotlinapp.Services

import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.User

object UserService {

    fun logout () {
        App.prefs.authToken = ""
        App.prefs.isLoggedIn = false
        App.prefs.userData = ""
        App.prefs.userHost = ""
    }

    fun updateUserData (user: User) {
        App.prefs.userData = user.toString()
    }
}