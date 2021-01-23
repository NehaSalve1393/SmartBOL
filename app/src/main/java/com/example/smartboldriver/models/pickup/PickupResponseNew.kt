package com.example.smartboldriver.models.pickup

import com.google.gson.annotations.SerializedName

data class PickupResponseNew
    (
    @SerializedName("sbolnum")  var sbolnum: String?,
    @SerializedName("txdate")  var txdate: String?,
    @SerializedName("shipdate")  var shipdate: String?,
    @SerializedName("srclocation")  var srclocation: String?,
    @SerializedName("doccode")  var doccode: String?,
    @SerializedName("picknum")   var picknum: String?,
    @SerializedName("delvnum")  var delvnum: String?,
    @SerializedName("bolnum")   var bolnum: String?,
    @SerializedName("shipnum")  var shipnum: String?,
    @SerializedName("shipper")  var shipper: String?,
    @SerializedName("shipadd1")  var shipadd1: String?,
    @SerializedName("shipadd2")   var shipadd2: String?,
    @SerializedName("shipcity")   var shipcity: String?,
    @SerializedName("shipstate")   var shipstate: String?,
    @SerializedName("shipzip")   var shipzip: String?,
    @SerializedName("shipcntry")   var shipcntry: String?,
    @SerializedName("shipacc")   var shipacc: String?,
    @SerializedName("consignee")   var consignee: String?,
@SerializedName("consadd1") val consadd1 : String?,
@SerializedName("consadd2")  val consadd2 : String?,
@SerializedName("conscity")  val conscity : String?,
@SerializedName("consstate")  val consstate : String?,
@SerializedName("conszip")  val conszip : String?,
@SerializedName("conscntry")   val conscntry : String?,
@SerializedName("consattn")   val consattn: String?,
@SerializedName("consacc")  val consacc: String?,
@SerializedName("hazmat")  val hazmat: String?,
@SerializedName("hmcontact") val hmcontact : String?,
@SerializedName("spinstr")  val spinstr : String?,
@SerializedName("stopnum")   val stopnum : String?,
@SerializedName("status")  val status : String?,
@SerializedName("downloaddate")  val downloaddate : String?,
@SerializedName("token")  val token : String?,
@SerializedName("path")  val path : String?,
@SerializedName("pronum")  val pronum : String?,
@SerializedName("comments")val comments: String?,
@SerializedName("pickup") val pickup : String?




)