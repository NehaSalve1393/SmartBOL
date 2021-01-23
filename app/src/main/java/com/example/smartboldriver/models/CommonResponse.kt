package com.example.smartboldriver.models

import com.google.gson.annotations.SerializedName

open class CommonResponse(

    @SerializedName("code") val code: Int = 0,


    @SerializedName("message") val message: String = ""
)