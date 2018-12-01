package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

object ReportForm {
    val userHostData = App.prefs.userHost
    var userHost = if (userHostData != "") Host(JSONObject(userHostData)) else null
    var title: String? = null
    var location: String? = null
    var description: String? = null
    var tags: String? = null
    var categoryName: String? = null
    var category: JSONObject? = null
    var urgency: String? = "LOW"
    var long: Double? = null
    var lat: Double? = null
    var img1: Bitmap? = null
    var img2: Bitmap? = null
    var img3: Bitmap? = null
    var img4: Bitmap? = null
    var hostListJson: JSONArray? = null
    var peopleList: JSONArray = JSONArray()
    var urgencyIndex: Int = 0
    var hostsIndex: Int = 0
    var hostName: String = if (userHost?.name != null) userHost?.name!! else ""
    var hostId: String = if (userHost?._id != null) userHost?._id!! else ""

    fun isValid (): Boolean {
        return title != "" && title != null
            && location != "" && location != null
            && description != "" && description != null
            && long != null
            && lat != null
            && tags != null && tags != ""
            && hostId != null && hostId != ""
            && categoryName != null && categoryName != ""
            && urgency != null && urgency != ""
    }

    fun clear () {
        title = ""
        location = ""
        description = ""
        categoryName = ""
        category = null
        hostId = ""
        tags = ""
        long = null
        lat = null
        img1 = null
        img2 = null
        img3 = null
        img4 = null
        peopleList = JSONArray()
        try {
            hostName = if (userHost?.name != null) userHost?.name!! else ""
            hostId = if (userHost?._id != null) userHost?._id!! else ""
        }
        catch (e: Exception) {
            hostName = ""
            hostId = ""
        }
    }
}