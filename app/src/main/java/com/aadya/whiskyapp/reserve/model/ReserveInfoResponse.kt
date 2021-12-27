package com.aadya.whiskyapp.reserve.model

import com.google.gson.annotations.SerializedName
import java.util.*

class ReserveInfoResponse {

     @SerializedName("bookingInfoID")
     var BookingInfoID: Int? = null

    @SerializedName("userID")
    var userID: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("numberofPeople")
    var numberofPeople: Int? = null

    @SerializedName("favorite")
    var favorite: String? = null


    @SerializedName("bookingDate")
    var bookingDate: String? = null

    @SerializedName("bookingTime")
    var bookingTime: String? = null

    @SerializedName("isActive")
    var isActive: Boolean? = null

    @SerializedName("approvedBy")
    var approvedBy: Int? = null


    @SerializedName("createdBy")
    var createdBy: Int? = null

    @SerializedName("bookingStatus")
    var bookingStatus: String? = null


    @SerializedName("otherDescription")
    var otherDescription: String? = null

    @SerializedName("approvedDate")
    var approvedDate: String? = null


    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("middleName")
    var middleName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null


    @SerializedName("email")
    var email: String? = null

    @SerializedName("agentID")
    var agentID: String? = null

    @SerializedName("photograph")
    var photograph: String? = null


    @SerializedName("bookingExpire")
    var bookingExpire: Boolean? = null

    @SerializedName("bookingInfo")
    var bookingInfo: Boolean?=null
    @SerializedName("isArrived")
    var isArrived: Boolean?=null
    @SerializedName("notes")
    var notes: String?=null

    @SerializedName("visitedDatetime")
    var visitedDatetime: String?=null


 }
