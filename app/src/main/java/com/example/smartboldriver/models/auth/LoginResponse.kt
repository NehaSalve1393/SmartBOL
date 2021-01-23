package com.example.smartboldriver.models.auth

import com.example.smartboldriver.models.CommonResponse
import com.google.gson.annotations.SerializedName

//data class LoginResponse()
data class LoginResponse(
    @SerializedName("retcode") val retcode: Int? = null,
    @SerializedName("account") val account: String? = "",
    @SerializedName("deviceid") val deviceid: String? = "",
    @SerializedName("user") val user: String? = "",
    @SerializedName("pass") val pass: String? = "",
    @SerializedName("issuer") val issuer: String? = "",
    @SerializedName("srclocation") val srclocation: String? = null,
    @SerializedName("txdate") val txdate: String? = null,
    @SerializedName("txtype") val txtype: String? = null,
    @SerializedName("txstatus") val txstatus: String? = null,
    @SerializedName("txnum") val txnum: String? = null,
    @SerializedName("txnum2") val txnum2: String? = null,
    @SerializedName("comments") val comments: String? = null,
    @SerializedName("dock") val dock: String? = null,
    @SerializedName("picknum") val picknum: String? = null,
    @SerializedName("sbolnum") val sbolnum: String? = null,
    @SerializedName("imgstr") val imgstr: String? = null,
    @SerializedName("imgstr2") val imgstr2: String? = null,
    @SerializedName("rev") val rev: String? = null
) : CommonResponse()

