package com.aadya.gist.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequestModel {



    @SerializedName("MemberLogin")
    var MemberLogin: String? = null

    @SerializedName("Password")
    var Password: String? = null

    @SerializedName("DeviceId")
    var DeviceId: String? = null

    @SerializedName("username")
    var username: String? = null


}