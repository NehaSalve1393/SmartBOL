package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName

data class SaveSignPdfRequest (
    @SerializedName("user")  var user: String?,
    @SerializedName("pass")  var pass: String?,
    @SerializedName("account")  var account: String?,
    @SerializedName("txtype")  var txtype: String?,

    @SerializedName("txnum")  var txnum: String?,
    @SerializedName("txnum2")  var txnum2: String?,
    @SerializedName("txdate")  var txdate: String?


    )