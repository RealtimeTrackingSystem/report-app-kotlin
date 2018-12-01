package com.johnhigginsmavila.rcrtskotlinapp.Model

import org.json.JSONObject

class Category {
    var name: String? = null
    var description: String? = null
    constructor()
    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    fun toJson () : JSONObject {
        val json = JSONObject()
        json.put("name", this.name)
        json.put("description", this.description)
        return json
    }
}