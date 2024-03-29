package com.aadya.whiskyapp.reserve.model
import com.google.gson.annotations.SerializedName

class ReserveRequestModel {

    @SerializedName("UserID")
    var UserID: Int? = null

    @SerializedName("NumberofPeople")
    var NumberofPeople: String? = null

    @SerializedName("Favorite")
    var Favorite: String? = null

    @SerializedName("BookingDate")
    var BookingDate: String? = null

    @SerializedName("BookingTime")
    var BookingTime : String? = null

    @SerializedName("BookingInfoID")
    var BookingInfoID : Int? = 0

    @SerializedName("Title")
    var Title : String? = null

    @SerializedName("Description")
    var Description : String? = null

}