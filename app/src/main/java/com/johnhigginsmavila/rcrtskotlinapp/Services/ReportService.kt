package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewReport
import com.johnhigginsmavila.rcrtskotlinapp.Model.Report
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.REPORT_URL
import io.reactivex.Observable
import org.json.JSONException
import java.lang.Exception
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.ByteArrayOutputStream

object ReportService {

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
}