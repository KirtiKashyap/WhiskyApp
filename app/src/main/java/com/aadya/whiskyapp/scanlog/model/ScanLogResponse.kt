package com.aadya.whiskyapp.scanlog.model

import com.google.gson.annotations.SerializedName

data class ScanLogResponse (
    @SerializedName("scanLogID")
    var menuID: Int,
    @SerializedName("scanDate")
    var scanDate: String,
    @SerializedName("scanTime")
    var scanTime: String,
    @SerializedName("scanedBy")
    var scanedBy: Int,
    @SerializedName("scanedByUser")
    var scanedByUser: String ?,
    @SerializedName("memberID")
    var memberID: Int ?
        )
