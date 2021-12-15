package com.aadya.whiskyapp.menu.model

import com.google.gson.annotations.SerializedName

data class MenuResponse(@SerializedName("menuID")
                        var menuID: Int,
                        @SerializedName("userID")
                        var userID: Int,
                        @SerializedName("title")
                        var title: String,
                        @SerializedName("description")
                        var description: String,
                        @SerializedName("isActive")
                        var isActive: Boolean,
                        @SerializedName("createdDate")
                        var createdDate: String,
                        @SerializedName("createdBy")
                        var createdBy: Int,
                        @SerializedName("imageName")
                        var imageName: String,
                        @SerializedName("urlofImage")
                        var urlofImage: String
)
