package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

object ReportForm {
    var title: String? = null
    var location: String? = null
    var description: String? = null
    var tags: String? = null
    var long: Double? = null
    var lat: Double? = null
    var img1: Bitmap? = null
    var img2: Bitmap? = null
    var img3: Bitmap? = null
    var img4: Bitmap? = null

    fun isValid (): Boolean {
        return title != "" && title != null
            && location != "" && location != null
            && description != "" && description != null
            && long != null
            && lat != null
            && tags != null && tags != ""
    }

    fun clear () {
        title = ""
        location = ""
        description = ""
        tags = ""
        long = null
        lat = null
        img1 = null
        img2 = null
        img3 = null
        img4 = null
    }
}