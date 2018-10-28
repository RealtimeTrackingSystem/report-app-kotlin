package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewUser
import com.johnhigginsmavila.rcrtskotlinapp.Model.User
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.LOGIN_URL
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SIGNUP_URL
import io.reactivex.Observable
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    fun registerUser (newUser: NewUser): Observable<Boolean> {
        return Observable.create{
            val jsonBody = newUser.toJSON()
            val requestBody = jsonBody.toString()

            val registerRequest = object : JsonObjectRequest(Method.POST, SIGNUP_URL, null, Response.Listener { response ->
                try {
                    Log.d("Response", response.toString())
                    if (response.get("httpCode").toString() == "200") {

                    }
                    val payload = response.getJSONObject("payload")
                    val userJson = payload.getJSONObject("user")
                    App.prefs.userData = userJson.toString()
                    App.prefs.authToken = payload.getString("token")
                    App.prefs.isLoggedIn = true
                    it.onNext(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    it.onNext(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not register user: $error")
                it.onError(error)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }

            App.prefs.requestQueue.add(registerRequest)
        }
    }

    fun loginUser (loginName: String, password: String): Observable<Boolean> {
        return Observable.create{
            val jsonBody = JSONObject()
            jsonBody.put("loginName", loginName)
            jsonBody.put("password", password)
            val requestBody = jsonBody.toString()

            val loginRequest = object: JsonObjectRequest(Method.POST, LOGIN_URL, null, Response.Listener { response ->
                try {
                    Log.d("Response", response.toString())
                    if (response.get("httpCode").toString() == "200") {

                    }
                    val payload = response.getJSONObject("payload")
                    val userJson = payload.getJSONObject("user")
                    App.prefs.userData = userJson.toString()
                    App.prefs.authToken = payload.getString("token")
                    App.prefs.isLoggedIn = true
                    it.onNext(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    it.onNext(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not log in user: $error")
                it.onNext(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }

            App.prefs.requestQueue.add(loginRequest)
        }
    }
}