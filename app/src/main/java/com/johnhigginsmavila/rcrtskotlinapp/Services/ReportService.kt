package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.*
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.REPORT_URL
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.ByteArrayOutputStream

object ReportService {

    var reports: ArrayList<Report> = ArrayList<Report>()

    fun sendReport (report: NewReport): Observable<Boolean> {
        val auth = App.prefs.authToken
        val headers = HashMap<String, String>()
        headers.put("Authorization", auth)
        return Observable.create {
            val reportRequest: MultipartRequest = MultipartRequest(REPORT_URL, headers, { response ->
                it.onNext(true)
            }, {errorResponse ->
                try {
                    Log.d("POST_REPORT", "An Error occured")
                } catch (e: Exception) {
                    Log.d("POST_REPORT", "An Error occured")
                }


                it.onNext(false)
            })
            var tags = report.tags!!.split(" ").joinToString(",")

            reportRequest.addPart(MultipartRequest.FormPart("title", report.title))
            reportRequest.addPart(MultipartRequest.FormPart("description", report.description))
            reportRequest.addPart(MultipartRequest.FormPart("location",  report.location))
            reportRequest.addPart(MultipartRequest.FormPart("long", report.long.toString()))
            reportRequest.addPart(MultipartRequest.FormPart("lat", report.lat.toString()))
            reportRequest.addPart(MultipartRequest.FormPart("tags", tags))

            for (item in report.medias) {
                var byte = ByteArrayOutputStream()
                item.compress(Bitmap.CompressFormat.JPEG, 90, byte)
                reportRequest.addPart(MultipartRequest.FilePart("reports", "image/jpeg", "${report.title}", byte.toByteArray()))
            }

            App.prefs.requestQueue.add(reportRequest)
        }
    }

    fun getMyReports (): Observable<Boolean> {
        return Observable.create {
            val getReportRequest  = object: JsonObjectRequest(Request.Method.GET, "$REPORT_URL?reporter=${App.prefs.reporterId}&resources=medias", null, Response.Listener { response ->
                val httpCode = response.getInt("httpCode")
                if (httpCode == 200) {
                    loadToast("${response.getJSONArray("reports").length()} Report(s) now Loading....")
                    loadDataFromReportResponse(it, response)
                } else {
                    loadToast(response.getString("message"))
                    it.onNext(false)
                }
            }, Response.ErrorListener { errorResponse ->
                loadToast("An error occurs while loading reports")
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

            App.prefs.requestQueue.add(getReportRequest)
        }
    }

    fun loadDataFromReportResponse (it: ObservableEmitter<Boolean>, response: JSONObject) {
        try {
            reports.clear()
            Log.d("REPORTS", response.toString())
            val reportsArray = response.getJSONArray("reports")
            for (index in 0..(reportsArray.length() -1)) {

                val mediaUploads: ArrayList<MediaUpload> = ArrayList<MediaUpload>()
                val people: ArrayList<Person> = ArrayList<Person>()
                val properties: ArrayList<Property> = ArrayList<Property>()

                val reportData: JSONObject = reportsArray.getJSONObject(index)
                val _id = reportData.getString("_id")
                val title = reportData.getString("title")
                val description = reportData.getString("description")
                val location = reportData.getString("location")
                val long = reportData.getDouble("long")
                val lat = reportData.getDouble("lat")
                val _reporter = reportData.getString("_reporter")
                val _host = reportData.getString("_host")
                val status = reportData.getString("status")
                val medias = reportData.getJSONArray("medias")
                val peopleArray = reportData.getJSONArray("people")
                val propertiesArray = reportData.getJSONArray("properties")
                val tagsArray = reportData.getJSONArray("tags")
                var tags = ""

                for (i in 0..(tagsArray.length() -1)) {
                    tags += tagsArray.getString(i)
                }

                for (c in 0..(medias.length() -1)) {
                    val m = medias.getJSONObject(c)
                    val media = MediaUpload(m.getJSONObject("metaData"))

                    mediaUploads.add(media)
                }
                val report = Report(_id, title, description, location, long, lat, tags, _reporter, _host, status, people, properties, mediaUploads)
                reports.add(report)
            }
            it.onNext(true)
        } catch (e: JSONException) {

            it.onNext(false)
        }
    }

    fun loadToast (text: String) {
        Toast.makeText(App.prefs.context, text, Toast.LENGTH_SHORT).show()
    }
}