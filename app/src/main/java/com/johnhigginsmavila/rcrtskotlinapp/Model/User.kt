package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.JsonReader
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class User (
    var _id: String = "",
    var reporterID: String = "",
    var accessLevel: String = "",
    var username: String = "",
    var email: String = "",
    var fname: String = "",
    var lname: String = "",
    var gender: String = "",
    var alias: String = "",
    var street: String = "",
    var barangay: String = "",
    var city: String = "",
    var region: String = "",
    var country: String = "",
    var zip: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var hosts: ArrayList<HostMember> = ArrayList()
) {

    fun convertToJsonArray (hosts: ArrayList<HostMember>): JSONArray {
        var array: JSONArray = JSONArray()
        for (i in 0 until hosts.count()) {
            val host = hosts[i].toJson()
            array.put(host)
        }
        return array
    }

    fun toJson (): JSONObject {
        val json = JSONObject()

        json.put("_id", this._id)
        json.put("reporterID", this.reporterID)
        json.put("accessLevel", this.accessLevel)
        json.put("username", this.username)
        json.put("fname", this.fname)
        json.put("lname", this.lname)
        json.put("gender", this.gender)
        json.put("alias", this.alias)
        json.put("street", this.street)
        json.put("barangay", this.barangay)
        json.put("city", this.city)
        json.put("region", this.region)
        json.put("country", this.country)
        json.put("zipt", this.zip)
        json.put("createdAt", this.createdAt)
        json.put("updatedAt", this.updatedAt)
        val hostsJsonArray: JSONArray = convertToJsonArray(this.hosts)
        json.put("hosts", hostsJsonArray.toString())
        return json
    }

    fun stringify (): String {
        return toJson().toString()
    }

}

/*
*
* "accessLevel": "USER",
    "_id": "5bc2148afd54cb000f368fdd",
    "username": "johnhiggins",
    "email": "johnhiggins.avila@gmail.com",
    "fname": "John Higgins",
    "lname": "Avila",
    "gender": "M",
    "alias": "John",
    "street": "1119",
    "barangay": "Payatas",
    "city": "Quezon City",
    "region": "NCR",
    "country": "Philippines",
    "zip": "1119",
* */