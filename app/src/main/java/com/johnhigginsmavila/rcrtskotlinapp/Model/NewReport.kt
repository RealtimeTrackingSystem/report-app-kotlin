package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.graphics.Bitmap

class NewReport (
    var title: String? = "",
    var description: String? = "",
    var location: String? = "",
    var long: Double? = 0.0,
    var lat: Double? = 0.0,
    var tags: String? = "",
    var medias: ArrayList<Bitmap> = ArrayList(),
    var people: ArrayList<Person> = ArrayList(),
    var properties: ArrayList<Property> = ArrayList()
)

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