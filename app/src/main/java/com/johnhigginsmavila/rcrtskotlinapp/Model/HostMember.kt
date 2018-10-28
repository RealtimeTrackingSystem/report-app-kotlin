package com.johnhigginsmavila.rcrtskotlinapp.Model

import org.json.JSONObject

class HostMember (
    var _id: String = "",
    var isOwner: Boolean = false,
    var isAdmin: Boolean = false,
    var isBlocked: Boolean = false,
    var createdAt: String = "",
    var updatedAt: String = "",
    var hostDetails: Host = Host()
) {
    fun loadFromJson (json: JSONObject): HostMember {
        _id = json.getString("_id")
        isOwner = json.getBoolean("isOwner")
        isAdmin = json.getBoolean("isAdmin")
        isBlocked = json.getBoolean("isBlocked")
        createdAt = json.getString("createdAt")
        updatedAt = json.getString("updatedAt")
        return this
    }

    fun loadHostDetailsFromJson (json: JSONObject): HostMember {
        hostDetails = Host().loadFromJson(json)
        return this
    }

    fun toJson (): JSONObject {
        val json = JSONObject()
        json.put("_id", this._id)
        json.put("isOwner", this.isOwner)
        json.put("isAdmin", this.isAdmin)
        json.put("isBlocked", this.isBlocked)
        json.put("createdAt", this.createdAt)
        json.put("hostDetails", "")
        return json
    }

    fun stringify(): String {
        return toJson().toString()
    }

}