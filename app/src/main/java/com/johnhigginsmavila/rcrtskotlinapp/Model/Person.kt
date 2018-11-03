package com.johnhigginsmavila.rcrtskotlinapp.Model

class Person (
    var fname: String = "",
    var lname: String = "",
    var alias: String = "",
    var isCulprit: Boolean = false,
    var isCasualty: Boolean = true
)

/***
_report: { type: Types.ObjectId, ref: 'Report', required: true },
fname: { type: String },
lname: { type: String },
alias: { type: String, required: true },
isCulprit: { type: Boolean, default: false },
isCasualty: { type: Boolean, default: true }
***/