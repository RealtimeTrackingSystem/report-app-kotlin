package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.HOST_URL
import io.reactivex.Observable
import org.json.JSONException
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
                    headers.put("Authorization", App.prefs.authToken)
                    return headers
                }
            }
            App.prefs.requestQueue.add(loadHost)
        }
    }
}