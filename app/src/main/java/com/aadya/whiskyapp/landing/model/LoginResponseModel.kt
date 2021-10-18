package com.aadya.whiskyapp.landing.model

import com.example.example.ObjListMemberLoginServiceModel
import com.google.gson.annotations.SerializedName


data class LoginResponseModel (

    @SerializedName("accessToken") var accessToken : String,
    @SerializedName("tokenType") var tokenType : String,
    @SerializedName("expiresIn") var expiresIn : Int,
    @SerializedName("issuedAt") var issuedAt : String,
    @SerializedName("expiresAt") var expiresAt : String,
    @SerializedName("statusCode") var statusCode : Int,
    @SerializedName("result") var result : String,
    @SerializedName("objListUserLoginServiceModel") var objListUserLoginServiceModel : String,
    @SerializedName("objListMemberLoginServiceModel") var objListMemberLoginServiceModel : ObjListMemberLoginServiceModel
)