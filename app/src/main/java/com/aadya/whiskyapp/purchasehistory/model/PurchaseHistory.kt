package com.aadya.whiskyapp.purchasehistory.model

import com.google.gson.annotations.SerializedName

data class PurchaseHistory (

    @SerializedName("paymentID") var paymentID : Int?,
    @SerializedName("userid") var userid : Int,
    @SerializedName("ticketID") var ticketID : Int,
    @SerializedName("itemType") var itemType : String,
    @SerializedName("itemID") var itemID : Int,
    @SerializedName("memberID") var memberID : Int,
    @SerializedName("paymentDate") var paymentDate : String,
    @SerializedName("paymentStatus") var paymentStatus : String,
    @SerializedName("amount") var amount : String,

    )