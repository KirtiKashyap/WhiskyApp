package com.aadya.whiskyapp.events.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EventsResponseModel(
    @SerializedName("eventID") val eventID: Int,
    @SerializedName("userID") val userID: Int,
    @SerializedName("eventTitle") val eventTitle: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("eventDate") val eventDate: String?,
    @SerializedName("eventTime") val eventTime: String?,
    @SerializedName("eventEndTime") val eventEndTime: String?,
    @SerializedName("eventStartTime") val eventStartTime: String?,
    @SerializedName("eventLocation") val eventLocation: String?,
    @SerializedName("eventLat") val eventLat: String?,
    @SerializedName("eventLong") val eventLong: String?,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("createdDate") val createdDate: String?,
    @SerializedName("createdBy") val createdBy: Int,
    @SerializedName("imageName") val imageName: String?,
    @SerializedName("imagePath") val imagePath: String?,
    @SerializedName("eventType") val eventType: Int
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(eventID)
        parcel.writeInt(userID)
        parcel.writeString(eventTitle)
        parcel.writeString(description)
        parcel.writeString(eventDate)
        parcel.writeString(eventTime)
        parcel.writeString(eventEndTime)
        parcel.writeString(eventStartTime)
        parcel.writeString(eventLocation)
        parcel.writeString(eventLat)
        parcel.writeString(eventLong)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(createdDate)
        parcel.writeInt(createdBy)
        parcel.writeString(imageName)
        parcel.writeString(imagePath)
        parcel.writeInt(eventType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventsResponseModel> {
        override fun createFromParcel(parcel: Parcel): EventsResponseModel {
            return EventsResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<EventsResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}