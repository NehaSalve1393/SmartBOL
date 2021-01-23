package com.example.smartboldriver.models.checkin

import com.google.gson.annotations.SerializedName

data class CheckInRequest (

    @SerializedName("txtype") val txtype: String,
    @SerializedName("user") val user: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("account") val account: String,
    @SerializedName("picknum") val picknum: String,
    @SerializedName("txstatus") val txstatus: String,
    @SerializedName("srclocation") val srclocation: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("carrier") val carrier: String,
    @SerializedName("txdate") val txdate: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String


)
