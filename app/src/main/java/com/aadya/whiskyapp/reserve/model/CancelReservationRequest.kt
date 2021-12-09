package com.aadya.whiskyapp.reserve.model

import com.google.gson.annotations.SerializedName

data class CancelReservationRequest(@SerializedName("BookingInfoID") var BookingInfoID : Int = 0,
                                    @SerializedName("BookingStatus") var BookingStatus : String,
                                    @SerializedName("OtherDescription") var OtherDescription : String
)

