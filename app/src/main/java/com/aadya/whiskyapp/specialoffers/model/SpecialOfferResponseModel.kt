package com.aadya.whiskyapp.specialoffers.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName





data class SpecialOfferResponseModel(

@SerializedName("specialOfferID") var specialOfferID : Int,
@SerializedName("userID") var userID : Int,
@SerializedName("liquorID") var liquorID : Int,
@SerializedName("title") var title : String?,
@SerializedName("description") var description : String?,
@SerializedName("isActive") var isActive : Boolean,
@SerializedName("createdDate") var createdDate : String?,
@SerializedName("createdBy") var createdBy : Int,
@SerializedName("price") var price : String?,
@SerializedName("yo") var yo : String?,
@SerializedName("fromDate") var fromDate : String?,
@SerializedName("toDate") var toDate : String?,
@SerializedName("imageName") var imageName : String?,
@SerializedName("urlofImage") var urlofImage : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(specialOfferID)
        parcel.writeInt(userID)
        parcel.writeInt(liquorID)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(createdDate)
        parcel.writeInt(createdBy)
        parcel.writeString(price)
        parcel.writeString(yo)
        parcel.writeString(fromDate)
        parcel.writeString(toDate)
        parcel.writeString(imageName)
        parcel.writeString(urlofImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpecialOfferResponseModel> {
        override fun createFromParcel(parcel: Parcel): SpecialOfferResponseModel {
            return SpecialOfferResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<SpecialOfferResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}