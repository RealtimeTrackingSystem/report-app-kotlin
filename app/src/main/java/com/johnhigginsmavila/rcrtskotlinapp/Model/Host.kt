package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Host {

    var _id: String? = null
    var name: String? = null
    var email: String? = null
    var location: String? = null
    var description: String? = null
    var hostNature: String? = null
    var defaultTags: ArrayList<String>? = null
    var long: Double? = null
    var lat: Double? = null
    // var type: String? = null
    var street: String? = null
    var barangay: String? = null
    var city: String? = null
    var region: String? = null
    var country: String? = null
    var zip: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null

    constructor()
    constructor(h: JSONObject) {
        try {
            _id = h.getString("_id")
            name = h.getString("name")
            location = h.getString("location")
            hostNature = h.getString("hostNature")
            email = h.getString("email")
            description = h.getString("description")

            var tags = h.getJSONArray("defaultTags")
            for (i in 0..(tags.length() -1)) {
                defaultTags?.add(tags.getString(i))
            }
            long = h.getDouble("long")
            lat = h.getDouble("lat")
            // type = h.getString("type")
            street = h.getString("street")
            barangay = h.getString("barangay")
            city = h.getString("city")
            region = h.getString("region")
            country = h.getString("country")
            zip = h.getString("zip")
            createdAt = h.getString("createdAt")
            updatedAt = h.getString("updatedAt")
        }
        catch (e: JSONException) {
            Log.d("HOST_CLASS_ERROR", e.localizedMessage)
        }
    }

    fun toJson (): JSONObject {
        val json = JSONObject()
        json.put("_id", this._id)
        json.put("name", this.name)
        json.put("email", this.email)
        json.put("location", this.location)
        json.put("hostNature", this.hostNature)
        json.put("defaultTags", defaultTags)
        json.put("long", long)
        json.put("lat", lat)
        // json.put("type", type)
        json.put("street", street)
        json.put("barangay", barangay)
        json.put("city", city)
        json.put("region", region)
        json.put("country", country)
        json.put("zip", zip)
        json.put("createdAt", createdAt)
        json.put("updatedAt", updatedAt)
        return json
    }

}

/**
 *
 * name: { type: String, unique: true, required: true },
    email: { type: String, unique: true, required: true  },
    location: { type: String },
    description: { type: String },
    hostNature: { type: String },
    defaultTags: [String],
    long: { type: Number },
    lat: { type: Number },
    hostCoordinates: {
    type: {type: String, enum: 'Point', default: 'Point'},
    coordinates: { type: [Number], default: [0, 0]}
    },
    street: { type: String },
    barangay: { type: String },
    city: { type: String },
    region: { type: String },
    country: { type: String },
    zip: { type: String }
 *
 *
 *
 */
