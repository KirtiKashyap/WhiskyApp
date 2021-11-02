package com.aadya.whiskyapp.payment.model

import com.google.gson.annotations.SerializedName

data class PaymentUpdate(@SerializedName("PaymentID") var paymentID : Int?,
                         @SerializedName("TicketID") var ticketID : String,//paymentIntentId
                         @SerializedName("ItemType") var itemType : String,
                         @SerializedName("ItemID") var itemID : Int,
                         @SerializedName("MemberID") var memberID : Int,
                         @SerializedName("PaymentDate") var paymentDate : String,
                         @SerializedName("PaymentStatus") var paymentStatus : String,
                         @SerializedName("Amount") var amount : String)
