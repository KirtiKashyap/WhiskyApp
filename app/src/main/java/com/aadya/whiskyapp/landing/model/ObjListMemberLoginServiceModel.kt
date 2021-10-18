package com.example.example

import com.google.gson.annotations.SerializedName

   
data class ObjListMemberLoginServiceModel (

   @SerializedName("memberID") var memberID : Int,
   @SerializedName("firstName") var firstName : String,
   @SerializedName("lastName") var lastName : String,
   @SerializedName("memberLogin") var memberLogin : String,
   @SerializedName("password") var password : String,
   @SerializedName("macAddress") var macAddress : String,
   @SerializedName("timeZone") var timeZone : String,
   @SerializedName("ime") var ime : String,
   @SerializedName("memberEmail") var memberEmail : String,
   @SerializedName("status") var status : Boolean

)