package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class Person {
    var fname: String? = null
    var lname: String? = null
    var alias: String? = null
    var isCulprit: Boolean? = null
    var isCasualty: Boolean? = null

    constructor()
    constructor(p: JSONObject) {
        try {
            fname = p.getString("fname")
            lname = p.getString("lname")
            alias = p.getString("alias")
            isCasualty = p.getBoolean("isCasualty")
            isCulprit = p.getBoolean("isCulprit")
        }
        catch (e: JSONException) {
            Log.d("PERSON_CLASS_ERROR", e.localizedMessage)
        }
    }
}

/***
_report: { type: Types.ObjectId, ref: 'Report', required: true },
fname: { type: String },
lname: { type: String },
alias: { type: String, required: true },
isCulprit: { type: Boolean, default: false },
isCasualty: { type: Boolean, default: true }
***/