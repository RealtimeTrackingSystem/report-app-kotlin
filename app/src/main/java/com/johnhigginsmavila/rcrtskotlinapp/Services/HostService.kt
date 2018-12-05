package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.HOST_REQUEST_URL
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.HOST_URL
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

object HostService {

    fun loadUserhost (_id: String): Observable<Boolean> {
        Log.d("HOST_ID", "this is the ID: $_id")
        if (_id == "") {
            return Observable.just(false)
        }
        return Observable.create{
            var loadHost = object: JsonObjectRequest(Method.GET, "$HOST_URL/$_id", null, Response.Listener { response ->
                try {
                    if (response.getInt("httpCode") == 200) {
                        Log.d("HOST_URL", "$HOST_URL/$_id")
                        Log.d("HOST_URL", response.toString())
                        val hostJson = response.getJSONObject("host")
                        App.prefs.userHost = hostJson.toString()
                        it.onNext(true)
                    } else {
                        it.onNext(false)
                    }
                }
                catch (e: JSONException) {
                    Log.d("HOST_URL", e.toString())
                    it.onNext(false)
                }
                catch (e: Exception) {
                    Log.d("ERROR", e.localizedMessage)
                    it.onNext(false)
                }
            }, Response.ErrorListener { response ->
                Log.d(  "HOST_URL", response.toString())
                it.onError(response)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    Log.d("AUTH_TOKEN", App.prefs.authToken)
                    if (App.prefs.authToken != null) {
                        headers.put("Authorization", App.prefs.authToken!!)
                    }
                    return headers
                }
            }
            App.prefs.requestQueue.add(loadHost)
        }
    }

    fun getHosts () : Observable<JSONArray> {
        return Observable.create{
            var loadHosts = object: JsonObjectRequest(Method.GET, "$HOST_URL?isApproved=true", null, Response.Listener { response ->
                try {
                    val statusCode = response.getInt("httpCode")
                    if (statusCode == 200) {
                        val hosts = response.getJSONArray("hosts")
                        it.onNext(hosts)
                    } else {
                        it.onNext(JSONArray())
                    }
                }
                catch (e: JSONException) {
                    Log.d("HOSTS_LIST", e.localizedMessage)
                    it.onNext(JSONArray())
                }
            }, Response.ErrorListener { error ->
                try {
                    Log.d("HOSTS_LIST", error.localizedMessage)
                    it.onNext(JSONArray())
                }
                catch (e: Exception) {
                    Log.d("HOSTS_LIST", error.localizedMessage)
                    it.onNext(JSONArray())
                }
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    if (App.prefs.authToken != null) {
                        headers.put("Authorization", App.prefs.authToken!!)
                    }
                    return headers
                }
            }
            App.prefs.requestQueue.add(loadHosts)
        }
    }

    fun sendHostRequest (host: Host): Observable<Boolean> {
        return Observable.create {
            var sendRequest = object: JsonObjectRequest(Method.POST, "${HOST_REQUEST_URL}/${host._id}", null, Response.Listener { response ->
                val httpCode = response.getInt("httpCode")
                if (httpCode >= 200 || httpCode < 202) {
                    it.onNext(true)
                } else {
                    Log.d("SEND_REQUEST_ERROR", "Cannot send request at this time")
                    it.onNext(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("SEND_REQUEST_ERROR", error.localizedMessage)
                it.onNext(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    if (App.prefs.authToken != null) {
                        headers.put("Authorization", App.prefs.authToken!!)
                    }
                    return headers
                }
            }
            App.prefs.requestQueue.add(sendRequest)
        }
    }
}