package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName

data class UploadSignRequest (
    @SerializedName("user")  var user: String?,
    @SerializedName("pass")  var pass: String?,
    @SerializedName("account")  var account: String?,
    @SerializedName("txtype")  var txtype: String?,

    @SerializedName("doccode")  var doccode: String?,
    @SerializedName("docname")  var docname: String?,

    @SerializedName("picknum")  var picknum: String?,
    @SerializedName("pickup")  var pickup: String?,
    @SerializedName("sbolnum")  var sbolnum: String?,
    @SerializedName("signedby")   var signedby: String?,
    @SerializedName("signgps")   var signgps: String?,
    @SerializedName("signhash")  var signhash: String?,

    @SerializedName("signtime")  var signtime: String?,
    @SerializedName("signinfo")   var signinfo: String?,

    @SerializedName("imgstr")  var imgstr: String?,

    @SerializedName("latitude")  var latitude: String?,
    @SerializedName("longitude")  var longitude: String?,


    @SerializedName("txdate")  var txdate: String?,

    @SerializedName("txstatus")   var txstatus: String?,
    @SerializedName("comment")   var comment: String?,
    @SerializedName("email")   var email: String?






)