package com.aadya.whiskyapp.dashboard.model

import com.google.gson.annotations.SerializedName

class LogoutRequest {
    @SerializedName("AccessToken")
    var AccessToken: String? = null
}