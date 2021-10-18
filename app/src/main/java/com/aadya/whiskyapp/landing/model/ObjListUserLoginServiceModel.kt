package com.aadya.whiskyapp.landing.model

import com.google.gson.annotations.SerializedName


data class ObjListUserLoginServiceModel (

    @SerializedName("userID") var userID : Int,
    @SerializedName("firstName") var firstName : String,
    @SerializedName("lastName") var lastName : String,
    @SerializedName("userLogin") var userLogin : String,
    @SerializedName("password") var password : String,
    @SerializedName("macAddress") var macAddress : String,
    @SerializedName("timeZone") var timeZone : String,
    @SerializedName("ime") var ime : String,
    @SerializedName("userEmail") var userEmail : String,
    @SerializedName("status") var status : Boolean

)