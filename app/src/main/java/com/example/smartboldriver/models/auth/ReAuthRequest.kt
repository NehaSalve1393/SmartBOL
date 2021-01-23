package com.example.smartboldriver.models.auth

import com.google.gson.annotations.SerializedName

data class ReAuthRequest (

    @SerializedName("txtype") val txtype: String,
    @SerializedName("account") val account: String,
    @SerializedName("user") val user: String,
    @SerializedName("issuer") val issuer: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("txdate") val txdate: String
)