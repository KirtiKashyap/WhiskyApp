package com.aadya.whiskyapp.payment.model

import com.google.gson.annotations.SerializedName

data class PaymentByEmail(@SerializedName("PayAmount") var payAmount : String,
                          @SerializedName("Currency") var currency : String,
                          @SerializedName("email") var email : String)
