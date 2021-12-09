package com.aadya.whiskyapp.reserve.model

import com.google.gson.annotations.SerializedName

data class ReserveInfoRequest(
    @SerializedName("MemberID") var memberID : Int = 0
)
