package com.johnhigginsmavila.rcrtskotlinapp.Model

class Property (
    var type: String = "",
    var owner: String = "",
    var description: String = "",
    var estimatedCost: Double = 0.0
)
/***
_report: { type: Types.ObjectId, ref: 'Report', required: true },
type: { type: String, required: true },
owner: { type: String },
description: { type: String },
estimatedCost: { type: Number }
 ***/