package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class Report {
    var _id: String? = null
    var title: String? = null
    var description: String? = null
    var location: String? = null
    var long: Double? = null
    var lat: Double? = null
    var tags: String? = null
    var category: JSONObject? = null
    var _reporter: String? = null
    var _host: String? = null
    var status: String? = null
    var people: ArrayList<Person>? = null
    var properties: ArrayList<Property>? = null
    var medias: ArrayList<MediaUpload>? = null
    var raw: JSONObject? = null

    constructor()
    constructor (reportData: JSONObject) {
        try {
            val mediaUploads: ArrayList<MediaUpload> = ArrayList<MediaUpload>()
            val people: ArrayList<Person> = ArrayList<Person>()
            val properties: ArrayList<Property> = ArrayList<Property>()

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
            val category = reportData.getJSONObject("category")
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

//            for (i in 0..(peopleArray.length() -1)) {
//                val person = Person(peopleArray.getJSONObject(i))
//
//                people.add(person)
//            }

            this._id = _id
            this.title = title
            this.description = description
            this.location = location
            this.long = long
            this.lat = lat
            this.tags = tags
            this._reporter = _reporter
            this._host = _host
            this.status = status
            this.people = people
            this.properties = properties
            this.medias = mediaUploads
            this.category = category
            this.raw = reportData
        }
        catch (e: JSONException) {
            Log.d("REPORT_CLASS_ERROR", e.localizedMessage)
        }
    }
    constructor(
        _id: String,
        title: String,
        description: String,
        location: String,
        long: Double,
        lat: Double,
        tags: String,
        _reporter: String,
        _host: String,
        status: String,
        people: ArrayList<Person>,
        properties: ArrayList<Property>,
        medias: ArrayList<MediaUpload>,
        category: JSONObject) {

        this._id = _id
        this.title = title
        this.description = description
        this.location = location
        this.long = long
        this.lat = lat
        this.tags = tags
        this._reporter = _reporter
        this._host = _host
        this.status = status
        this.people = people
        this.properties = properties
        this.medias = medias
        this.category = category
    }
}

/*
*
* title: { type: String, required: true },
  description: { type: String },
  location: { type: String },
  long: { type: Number },
  lat: { type: Number },
  _reporter: { type:Types.ObjectId, ref: 'Reporter', required: true },
  _host: { type: Types.ObjectId, ref: 'Host' },
  status: { type: String, enum: [ 'NEW', 'VALIDATED', 'INPROGRESS', 'DONE', 'EXPIRED'], default: 'NEW' },
  reportCoordinates: {
    type: {type: String, enum: 'Point', default: 'Point'},
    coordinates: { type: [Number], default: [0, 0]}
  },
  people: [{
    type: Types.ObjectId, ref: 'Person'
  }],
  properties: [{
    type: Types.ObjectId, ref: 'Property'
  }],
  medias: [{
    type: Types.ObjectId, ref: 'Media'
  }],
  tags: [String]
*
* */