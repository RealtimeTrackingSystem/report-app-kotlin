package com.johnhigginsmavila.rcrtskotlinapp.Model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class HostMember {
    var _id: String? = null
    var isOwner: Boolean? = null
    var isAdmin: Boolean? = null
    var isBlocked: Boolean? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var hostDetails: Host? = null

    constructor()
    constructor(hm: JSONObject, host: JSONObject) {
        try {
            _id = hm.getString("_id")
            isOwner = hm.getBoolean("isOwner")
            isBlocked = hm.getBoolean("isBlocked")
            isAdmin = hm.getBoolean("isAdmin")
            createdAt = hm.getString("createdAt")
            updatedAt = hm.getString("updatedAt")
            hostDetails = Host(host)
        }
        catch (e: JSONException) {
            Log.d("HOST_MEMBER_CLASS_ERROR", e.localizedMessage)
        }
    }

    fun toJson (): JSONObject {
        val json = JSONObject()
        json.put("_id", this._id)
        json.put("isOwner", this.isOwner)
        json.put("isAdmin", this.isAdmin)
        json.put("isBlocked", this.isBlocked)
        json.put("createdAt", this.createdAt)
        json.put("hostDetails", hostDetails?.toJson())
        return json
    }

    fun stringify(): String {
        return toJson().toString()
    }

}