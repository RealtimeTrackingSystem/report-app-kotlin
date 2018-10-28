package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.HOST_URL
import io.reactivex.Observable
import org.json.JSONException

object HostService {

    fun getHostById (_id: String): Observable<Host> {
        return Observable.create{
            var login = object: JsonObjectRequest(Method.GET, HOST_URL, null, Response.Listener { response ->
                try {
                    val json = response.getJSONObject("host")
                    var host = Host().loadFromJson(json)
                    it.onNext(host)
                }
                catch (e: JSONException) {
                    Log.d("HOST_URL", e.toString())
                    it.onError(e)
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
        }
    }
}