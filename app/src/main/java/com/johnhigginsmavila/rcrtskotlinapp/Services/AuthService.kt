package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewUser
import com.johnhigginsmavila.rcrtskotlinapp.Model.User
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.LOGIN_URL
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.REFRESH_USER_URL
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SIGNUP_URL
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var authResponseError: String? = null
    fun loadUserData (it: ObservableEmitter<Boolean>, response: JSONObject) {
        try {
            Log.d("Response", response.toString())
            if (response.get("httpCode").toString() == "200") {

            }
            val payload = response.getJSONObject("payload")
            val userJson = payload.getJSONObject("user")

            App.prefs.reporterId = userJson.getString("reporterID")
            App.prefs.userData = userJson.toString()
            App.prefs.authToken = payload.getString("token")
            Log.d("AUTH", App.prefs.reporterId)
            Log.d("AUTH", userJson.getString("reporterID"))
            App.prefs.isLoggedIn = true
            it.onNext(true)
        } catch (e: JSONException) {
            Log.d("JSON", "EXC:" + e.localizedMessage)
            it.onNext(false)
        }
    }

    private fun processHostData (it: Boolean): Observable<Boolean> {
        return when(it) {
            false -> Observable.just(false)
            true -> {
                try {
                    val userData = JSONObject(App.prefs.userData)
                    val hostArray = userData.getJSONArray("hosts")
                    if (hostArray.length() > 0) {
                        val host = hostArray.getJSONObject(0)
                        val id = host.getString("_id")
                        HostService.loadUserhost(id)
                    } else {
                        Observable.just(true)
                    }
                } catch (e: JSONException) {
                    Observable.just(false)
                }
            }
        }
    }

    fun registerUser (newUser: NewUser): Observable<Boolean> {
        return Observable.create{
            val jsonBody = newUser.toJSON()
            val requestBody = jsonBody.toString()

            val registerRequest = object : JsonObjectRequest(Method.POST, SIGNUP_URL, null, Response.Listener { response ->
                loadUserData(it, response)
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
                loadUserData(it, response)
            }, Response.ErrorListener { error ->
                try {
                    if (error.networkResponse.statusCode == 401) {
                        authResponseError = "Incorrect Credentials"
                    } else {
                        authResponseError = "Internal Server Error"
                    }

                    Log.d("ERROR", "Could not log in user: $error")
                    it.onNext(false)
                } catch (e: Exception) {
                    Log.d("ERROR", "Could not log in user: ${e.localizedMessage}")
                }
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
    fun loginUserAndLoadHost (loginName: String, password: String): Observable<Boolean> {
        return loginUser(loginName, password)
            .subscribeOn(Schedulers.io())
            .flatMap {
                processHostData(it)
            }
    }

    fun refreshUserData (): Observable<Boolean> {
        return Observable.create {
            var refreshUser = object: JsonObjectRequest(Method.GET, REFRESH_USER_URL, null, Response.Listener { response ->
                loadUserData(it, response)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not log in user: $error")
                it.onNext(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", App.prefs.authToken)
                    return headers
                }
            }
            App.prefs.requestQueue.add(refreshUser)
        }
    }

    fun refreshUserDataAndLoadHost (): Observable<Boolean> {
        return refreshUserData()
            .subscribeOn(Schedulers.io())
            .flatMap {
                processHostData(it)
            }
    }

}