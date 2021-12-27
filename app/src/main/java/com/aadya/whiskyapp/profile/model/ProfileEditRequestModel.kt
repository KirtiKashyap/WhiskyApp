package com.aadya.whiskyapp.profile.model

import com.google.gson.annotations.SerializedName

data class ProfileEditRequestModel (


    @SerializedName("MemberID") var MemberID : Int?,
    @SerializedName("FirstName") var firstName : String,
    @SerializedName("LastName") var lastName : String,
    @SerializedName("MiddleName") var middleName : String,
    @SerializedName("DateOfBirth") var dateOfBirth : String,
    @SerializedName("PhoneNumber") var phoneNo : String,
    @SerializedName("Address") var address : String,
    @SerializedName("email") var email : String,
    @SerializedName("Occupation") var Occupation : String,
    @SerializedName("SpouseName") var SpouseName : String,
    @SerializedName("TypeofMembership") var TypeofMembership : String,
    @SerializedName("AliasID") var AliasID : String,
    @SerializedName("FavoriteCocktail") var FavoriteCocktail : String,
    @SerializedName("AgentID") var AgentID : String


)