package com.example.smartboldriver.models.auth

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("txtype") val txtype: String,
    @SerializedName("user") val user: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("carrier") val carrier: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("account") val account: String,
    @SerializedName("idnum") val idnum: String,
    @SerializedName("txdate") val txdate: String,
    @SerializedName("txstatus") val txstatus: String,

    @SerializedName("comments") val comments: String
)