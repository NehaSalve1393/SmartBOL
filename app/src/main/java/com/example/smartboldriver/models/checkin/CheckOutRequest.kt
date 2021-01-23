package com.example.smartboldriver.models.checkin

import com.google.gson.annotations.SerializedName

class CheckOutRequest (
    @SerializedName("txtype") val txtype: String,
    @SerializedName("user") val user: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("account") val account: String,
    @SerializedName("picknum") val picknum: String,
    @SerializedName("txstatus") val txstatus: String,
    @SerializedName("srclocation") val srclocation: String,
    @SerializedName("signedby") val signedby: String,
    @SerializedName("signinfo   ") val signinfo: String,
    @SerializedName("signtime") val signtime: String,
    @SerializedName("imgstr") val imgstr: String,
    @SerializedName("imgstr2") val imgstr2: String,
    @SerializedName("txdate") val txdate: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String

)