package com.example.smartboldriver.models.pickup

import com.google.gson.annotations.SerializedName

data class GetPickupListRequest(
    @SerializedName("txtype") val txtype: String,
    @SerializedName("account") val account: String,
    @SerializedName("txdate") val txdate: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("user") val user: String,
    @SerializedName("picknum") val picknum: String,
    @SerializedName("sbolnum") val sbolnum: String,
    @SerializedName("txnum") val txnum: String


)