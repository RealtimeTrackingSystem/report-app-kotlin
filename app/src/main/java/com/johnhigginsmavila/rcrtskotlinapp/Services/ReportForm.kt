package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray

object ReportForm {
    var title: String? = null
    var location: String? = null
    var description: String? = null
    var hostId: String? = null
    var tags: String? = null
    var category: String? = null
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

    fun isValid (): Boolean {
        return title != "" && title != null
            && location != "" && location != null
            && description != "" && description != null
            && long != null
            && lat != null
            && tags != null && tags != ""
            && hostId != null && hostId != ""
            && category != null && category != ""
            && urgency != null && urgency != ""
    }

    fun clear () {
        title = ""
        location = ""
        description = ""
        category = ""
        hostId = ""
        tags = ""
        long = null
        lat = null
        img1 = null
        img2 = null
        img3 = null
        img4 = null
        peopleList = JSONArray()
    }
}