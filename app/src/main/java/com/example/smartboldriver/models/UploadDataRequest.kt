package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName

data class UploadDataRequest (
    @SerializedName("user")  var user: String?,
    @SerializedName("pass")  var pass: String?,
    @SerializedName("txtype")  var txtype: String?,
    @SerializedName("doccode")  var doccode: String?,
    @SerializedName("docname")  var docname: String?,
    @SerializedName("filetype")   var filetype: String?,
    @SerializedName("filename")  var filename: String?,

    @SerializedName("picknum")  var picknum: String?,
    @SerializedName("sbolnum")  var sbolnum: String?,
    @SerializedName("trailer")  var trailer: String?,
    @SerializedName("shipnum")  var shipnum: String?,
    @SerializedName("bolnum")  var bolnum: String?,
    @SerializedName("txstatus")  var txstatus: String?,
    @SerializedName("comments")  var comments: String?,
    @SerializedName("shipper")  var shipper: String?,

    @SerializedName("signgps")  var signgps: String?,
    @SerializedName("latitude")  var latitude: String?,
    @SerializedName("longitude")  var longitude: String?,
    @SerializedName("shipperacc")  var shipperacc: String?,
    @SerializedName("pronum")  var pronum: String?,
    @SerializedName("consignee")  var consignee: String?,
    @SerializedName("conscity")  var conscity: String?,
    @SerializedName("consstate")  var consstate: String?,

    @SerializedName("pickup")  var pickup: String?,
    @SerializedName("imgstr")  var imgstr: String?,
    @SerializedName("account")  var account: String?,
    @SerializedName("txdate")  var txdate: String?





)