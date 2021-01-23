package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName

data class QueueObject(

    @SerializedName("type") val type: String = "",
    @SerializedName("timestamp") val timestamp: String = "",
    @SerializedName("uploadtimestamp") val uploadtimestamp: String = "",
    @SerializedName("path") val path: String,
    @SerializedName("status") val status: String = "",
    @SerializedName("shipment") val shipment: String = "",
    @SerializedName("trailer") val trailer: String = "",
    @SerializedName("bol") val bol: String = "",
    @SerializedName("comment") val comment: String = "",
    @SerializedName("sbolnum") val sbolnum: String = "",
    @SerializedName("signedby") val signedby: String = "",
    @SerializedName("gps") val gps: String = "",
    @SerializedName("signhash") val signhash: String = "",
    @SerializedName("signinfo") val signinfo: String = "",
    @SerializedName("signimage") val signimage: String = "",
    @SerializedName("token") val token: String = "",
    @SerializedName("doccode") val doccode: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("picknum") val picknum: String = "",
    @SerializedName("exception") val exception: String = "",
    @SerializedName("account") val account: String = "",
    @SerializedName("pro") val pro: String = "",
    @SerializedName("cons") val cons: String = "",
    @SerializedName("conscity") val conscity: String = "",
    @SerializedName("consstate") val consstate: String = "",
    @SerializedName("pickup") val pickup: String = ""





)