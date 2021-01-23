package com.example.smartboldriver.models.auth

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("account") val account: String,
    @SerializedName("txtype") val txtype: String,
    @SerializedName("issuer") val issuer: String,
    @SerializedName("user") val user: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("idnum") val idnum: String,
    @SerializedName("comments") val comments: String,
    @SerializedName("txdate") val txdate: String
)