package com.aadya.whiskyapp.profile.model

import com.google.gson.annotations.SerializedName


data class ProfileResponseModel (

    @SerializedName("memberID") var memberID : Int,
    @SerializedName("memberTypeName") var memberTypeName : String,
    @SerializedName("firstName") var firstName : String,
    @SerializedName("userName") var userName : String,
    @SerializedName("middleName") var middleName : String,
    @SerializedName("lastName") var lastName : String,
    @SerializedName("dateOfBirth") var dateOfBirth : String,
    @SerializedName("memberLogin") var memberLogin : String,
    @SerializedName("password") var password : String,
    @SerializedName("address") var address : String,
    @SerializedName("occupation") var occupation : String,
    @SerializedName("phoneNumber") var phoneNumber : String,
    @SerializedName("email") var email : String,
    @SerializedName("spouseName") var spouseName : String,
    @SerializedName("agentID") var agentID : String,
    @SerializedName("aliasID") var aliasID : String,
    @SerializedName("photograph") var photograph : String,
    @SerializedName("qrCode") var qrCode : String,
    @SerializedName("favoritecocktail") var favoritecocktail : String,
    @SerializedName("description") var description : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("bookingStatus") var bookingStatus : Boolean,
    @SerializedName("favorite") var favorite : String,
    @SerializedName("passwordSalt") var passwordSalt : String,
    @SerializedName("userLoginTime") var userLoginTime : String

)