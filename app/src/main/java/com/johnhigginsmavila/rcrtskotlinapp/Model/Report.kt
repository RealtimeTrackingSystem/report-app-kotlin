package com.johnhigginsmavila.rcrtskotlinapp.Model

class Report (
    val _id: String,
    val title: String,
    val description: String,
    val location: String,
    val long: Double,
    val lat: Double,
    val tags: String,
    val _reporter: String,
    val _host: String,
    val status: String,
    val people: ArrayList<Person>,
    val properties: ArrayList<Property>,
    val medias: ArrayList<MediaUpload>
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