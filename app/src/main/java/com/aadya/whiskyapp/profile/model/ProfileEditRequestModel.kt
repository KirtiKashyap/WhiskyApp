package com.aadya.whiskyapp.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileEditRequestModel (

    @SerializedName("userID") var userID : Int?,
    @SerializedName("phoneNo") var phoneNo : String,
    @SerializedName("email") var email : String,
    @SerializedName("dateOfBirth") var dateOfBirth : String,
    @SerializedName("address") var address : String

)