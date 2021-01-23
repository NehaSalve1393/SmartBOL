package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName


data class User(



    var deviceId: String? = null,
    var token: String? = null,
    var userId: String? = null,
    var password: String? = null,
    var encryption: String? = null,
    var companyCode: String? = null,
    var account: String? = null


) : CommonResponse()