package com.aadya.whiskyapp.profile.model

import com.google.gson.annotations.SerializedName

data class UploadProfileImageRequest (@SerializedName("MemberID") var MemberID: Int?,@SerializedName("Photograph") var Photograph: Int?)