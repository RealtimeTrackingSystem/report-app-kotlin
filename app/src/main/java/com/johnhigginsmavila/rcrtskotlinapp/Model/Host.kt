package com.johnhigginsmavila.rcrtskotlinapp.Model

import org.json.JSONArray
import org.json.JSONObject

class Host (
    var _id: String = "",
    var name: String = "",
    var location: String = "",
    var description: String = "",
    var hostNature: String = "",
    var defaultTags: MutableList<String> = mutableListOf(),
    var long: String = "",
    var lat: String = "",
    var type: String = "",
    var street: String = "",
    var barangay: String = "",
    var city: String = "",
    var region: String = "",
    var country: String = "",
    var zip: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
) {
    fun loadFromJson (json: JSONObject) : Host {
        _id = json.getString("_id")
        name = json.getString("name")
        location = json.getString("location")
        description = json.getString("location")
        hostNature = json.getString("location")
        defaultTags = getDefaultTags(json.getJSONArray("defaultTags"))
        long = json.getString("location")
        lat = json.getString("location")
        type = json.getString("location")
        street = json.getString("location")
        barangay = json.getString("location")
        city = json.getString("location")
        region = json.getString("location")
        country = json.getString("location")
        zip = json.getString("location")
        createdAt = json.getString("location")
        updatedAt = json.getString("location")
        return this
    }

    fun getDefaultTags (tags: JSONArray) : MutableList<String> {
        var list: MutableList<String> = mutableListOf()
        for (i in 0..(tags.length() -1)) list.add(i, tags[i].toString())
        return list
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
