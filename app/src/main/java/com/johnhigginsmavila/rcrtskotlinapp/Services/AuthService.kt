package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewUser
import com.johnhigginsmavila.rcrtskotlinapp.Model.User
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.ByteArrayOutputStream
import java.lang.NullPointerException

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
                try {
                    val err = JSONObject(String(error.networkResponse.data))

                    authResponseError = err.getString("message")
                    it.onNext(false)
                } catch (e: JSONException) {
                    Log.d("SIGNUP_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                } catch (e: VolleyError) {
                    Log.d("SIGNUP_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: NullPointerException) {
                    Log.d("SIGNUP_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                }
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

    fun resetPassword (email: String): Observable<Boolean> {

        return Observable.create {
            val jsonBody = JSONObject()
            jsonBody.put("email", email)
            val requestBody = jsonBody.toString()
            val resetPassword = object: JsonObjectRequest(Method.POST, FORGOT_PASSWORD_URL, null, Response.Listener { response ->
                it.onNext(true)
            }, Response.ErrorListener { error ->
                try {
                    val err = JSONObject(String(error.networkResponse.data))

                    authResponseError = err.getString("message")
                    it.onNext(false)
                } catch (e: JSONException) {
                    Log.d("RESET_PASSWORD_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                } catch (e: NullPointerException) {
                    Log.d("RESET_PASSWORD_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                }
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }

            App.prefs.requestQueue.add(resetPassword)
        }
    }

    fun changePassword (oldPassword: String, password: String, passwordConfirmation: String): Observable<Boolean> {
        return Observable.create {
            val jsonBody = JSONObject()
            jsonBody.put("oldPassword", oldPassword)
            jsonBody.put("password", password)
            jsonBody.put("passwordConfirmation", passwordConfirmation)
            val requestBody = jsonBody.toString()

            val changePasswordRequest = object: JsonObjectRequest(Method.PUT, FORGOT_PASSWORD_URL, null, Response.Listener { response ->
                it.onNext(true)
            }, Response.ErrorListener { error ->
                try {
                    val err = JSONObject(String(error.networkResponse.data))

                    authResponseError = err.getString("message")
                    it.onNext(false)
                } catch (e: JSONException) {
                    Log.d("RESET_PASSWORD_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                } catch (e: NullPointerException) {
                    Log.d("RESET_PASSWORD_ERROR", e.localizedMessage)
                    authResponseError = "Internal Server Error"
                    it.onNext(false)
                }
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", App.prefs.authToken)
                    return headers
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }

            App.prefs.requestQueue.add(changePasswordRequest)
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

    fun updateProfileDetails (user: User): Observable<Boolean> {
        return Observable.create {

            val requestBody = user.toJson().toString()

            var updateUser = object: JsonObjectRequest(Method.PUT, UPDATE_USER_URL, null, Response.Listener { response ->
                it.onNext(true)
            }, Response.ErrorListener { error ->
                try {
                    val err = JSONObject(String(error.networkResponse.data))

                    authResponseError = err.getString("message")
                    it.onNext(false)
                } catch (e: JSONException) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: VolleyError) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: NullPointerException) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                }
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", App.prefs.authToken)
                    return headers
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
            App.prefs.requestQueue.add(updateUser)
        }
    }

    fun updateProfilePicture (photo: Bitmap) : Observable<Boolean> {
        return Observable.create {
            val auth = App.prefs.authToken
            val headers = HashMap<String, String>()
            headers.put("Authorization", auth)
            val changeProfilePic: MultipartRequest = MultipartRequest(UPDATE_PROFILE_PIC_URL, headers, { response ->
                Log.d("REPONSE", response.toString())
                it.onNext(true)
            }, { error ->
                try {
                    val err = JSONObject(String(error.networkResponse.data))
                    authResponseError = err.getString("message")
                    it.onNext(false)
                } catch (e: JSONException) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: VolleyError) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: NullPointerException) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                } catch (e: Exception) {
                    Log.d("UPDATE_PROFILE_ERROR", e.localizedMessage)
                    authResponseError = "Slow Internet Connection"
                    it.onNext(false)
                }
            })

            var byte = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 90, byte)
            val fileName = App.prefs.reporterId
            changeProfilePic.addPart(MultipartRequest.FilePart("profilepic", "image/jpeg", fileName, byte.toByteArray()))

            App.prefs.requestQueue.add(changeProfilePic)
        }
    }

}