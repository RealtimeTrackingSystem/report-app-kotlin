package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.EXTRA_REPORT_DETAIL_JSON
import kotlinx.android.synthetic.main.activity_report_detail.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ReportDetailActivity : AppCompatActivity() {

    lateinit var reportJson: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        val reportJsonString = intent.getStringExtra(EXTRA_REPORT_DETAIL_JSON)

        reportJson = JSONObject(reportJsonString)

        Log.d("DATA_LOADED", reportJson.toString())

        loadData()
        loadImage()
    }

    fun loadData () {
        reportIdTxt.text = reportJson.getString("_id")
        titleTxt.text = reportJson.getString("title")
        descriptionTxt.text = reportJson.getString("description")
        val reporter = reportJson.getJSONObject("_reporter")
        val host = reportJson.getJSONObject("_host")
        val category = reportJson.getJSONObject("category")
        reporterTxt.text = "${reporter.getString("fname")}  ${reporter.getString("lname")}"
        coordinatesTxt.text = "(long ${reportJson.getString("long")}, lat ${reportJson.getString("lat")})"
        hostTxt.text = host.getString("name")
        categoryTxt.text = category.getString("name")
        statusTxt.text = reportJson.getString("status")
        urgencyTxt.text = reportJson.getString("urgency")
    }

    fun loadImage () {
        try {
            val images: JSONArray = reportJson.getJSONArray("medias")
            if (images.length() > 0) {
                imagesScrollView.visibility = View.VISIBLE
                for (i in 0..(images.length() - 1)) {
                    when (i + 1) {
                        1 -> {
                            val imgJson = images.getJSONObject(i)
                            val metaData = imgJson.getJSONObject("metaData")
                            val uri = metaData.getString("secure_url")
                            Glide.with(this).load(Uri.parse(uri)).into(img1)
                            img1.visibility = View.VISIBLE
                        }
                        2 -> {
                            val imgJson = images.getJSONObject(i)
                            val metaData = imgJson.getJSONObject("metaData")
                            val uri = metaData.getString("secure_url")
                            Glide.with(this).load(Uri.parse(uri)).into(img2)
                            img2.visibility = View.VISIBLE
                        }
                        3 -> {
                            val imgJson = images.getJSONObject(i)
                            val metaData = imgJson.getJSONObject("metaData")
                            val uri = metaData.getString("secure_url")
                            Glide.with(this).load(Uri.parse(uri)).into(img3)
                            img3.visibility = View.VISIBLE
                        }
                        4 -> {
                            val imgJson = images.getJSONObject(i)
                            val metaData = imgJson.getJSONObject("metaData")
                            val uri = metaData.getString("secure_url")
                            Glide.with(this).load(Uri.parse(uri)).into(img4)
                            img4.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.d("LOAD_IMAGES_ERROR", e.localizedMessage)
        }
    }
}
