package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.JsonReader
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class User {
    var _id: String = ""
    var reporterID: String = ""
    var accessLevel: String = ""
    var username: String = ""
    var email: String = ""
    var fname: String = ""
    var lname: String = ""
    var age: Int = 0
    var gender: String = ""
    var alias: String = ""
    var street: String = ""
    var barangay: String = ""
    var city: String = ""
    var region: String = ""
    var country: String = ""
    var zip: String = ""
    var createdAt: String = ""
    var updatedAt: String = ""
    var profilePicture: JSONObject = JSONObject()
    var hosts: ArrayList<HostMember> = ArrayList()

    constructor()
    constructor(
        _id: String,
        reporterID: String,
        accessLevel: String,
        username: String,
        email: String,
        fname: String,
        lname: String,
        age: Int,
        gender: String,
        alias: String,
        street: String,
        barangay: String,
        city: String,
        region: String,
        country: String,
        zip: String,
        createdAt: String,
        updatedAt: String,
        profilePicture: JSONObject,
        hosts: ArrayList<HostMember> = ArrayList()
    ) {
        this._id = _id
        this.reporterID = reporterID
        this.accessLevel = accessLevel
        this.username = username
        this.email = email
        this.fname = fname
        this.lname = lname
        this.age = age
        this.gender = gender
        this.alias = alias
        this.street = street
        this.barangay = barangay
        this.city = city
        this.region = region
        this.country = country
        this.zip = zip
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.profilePicture = profilePicture
        this.hosts = hosts
    }
    constructor(user: JSONObject) {
        try {
            this._id = user.getString("_id")
            this.reporterID = user.getString("reporterID")
            this.accessLevel = user.getString("accessLevel")
            this.username = user.getString("username")
            this.email = user.getString("email")
            this.fname = user.getString("fname")
            this.lname = user.getString("lname")
            this.gender = user.getString("gender")
            this.alias = user.getString("alias")
            this.street = user.getString("street")
            this.barangay = user.getString("barangay")
            this.city = user.getString("city")
            this.region = user.getString("region")
            this.country = user.getString("country")
            this.zip = user.getString("zip")
            this.createdAt = user.getString("createdAt")
            this.updatedAt = user.getString("updatedAt")
            this.age = user.getInt("age")
            this.profilePicture = user.getJSONObject("profilePicture")
        }
        catch (e: JSONException) {
            Log.d("USER_CLASS_ERROR", e.localizedMessage)
        }
    }

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
        json.put("email", this.email)
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
        json.put("zip", this.zip)
        json.put("createdAt", this.createdAt)
        json.put("updatedAt", this.updatedAt)
        json.put("age", this.age)
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