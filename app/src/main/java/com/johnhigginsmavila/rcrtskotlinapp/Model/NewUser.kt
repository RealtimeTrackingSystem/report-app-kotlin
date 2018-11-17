package com.johnhigginsmavila.rcrtskotlinapp.Model

import org.json.JSONObject

class NewUser (
    var username: String = "",
    var email: String = "",
    var fname: String = "",
    var lname: String = "",
    var age: Int = 0,
    var gender: String = "",
    var alias: String = "",
    var street: String = "",
    var barangay: String = "",
    var city: String = "",
    var region: String = "",
    var country: String = "",
    var zip: String = "",
    var password: String = "",
    var passwordConfirmation: String = ""
) {
    fun isValid () : Boolean {
        return username != ""
            && email != ""
            && fname != ""
            && lname != ""
            && gender != ""
            && alias != ""
            && street != ""
            && barangay != ""
            && city != ""
            && region != ""
            && country != ""
            && zip != ""
            && password != ""
            && passwordConfirmation != ""
            && age > 0
    }

    fun toJSON () : JSONObject {
        val json = JSONObject()
        json.put("username", username)
        json.put("email", email)
        json.put("fname", fname)
        json.put("lname", lname)
        json.put("gender", gender)
        json.put("alias", alias)
        json.put("street", street)
        json.put("barangay", barangay)
        json.put("city", city)
        json.put("region", region)
        json.put("country", country)
        json.put("zip", zip)
        json.put("password", password)
        json.put("age", age)
        json.put("passwordConfirmation", passwordConfirmation)
        return json
    }
}