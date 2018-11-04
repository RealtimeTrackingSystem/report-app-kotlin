package com.johnhigginsmavila.rcrtskotlinapp.Model

import org.json.JSONObject

class MediaUpload (
    var metaData: JSONObject,
    var platform: String = "cloudinary"
) {

}
/***
 * _report: { type: Types.ObjectId, ref: 'Report', required: true },
 * platform: { type: String, required: true },
 * metaData: { type:String, required: true }
 */
