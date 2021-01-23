package com.example.smartboldriver.features.authentication

import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.auth.LoginResponse

class LoginContract {
    interface Presenter : BlueprintContract.Presenter<View> {

        fun login(account:String,txtype:String,issuer:String,user:String,password:String,
                  lng:String,lat:String,idnum:String,comments:String,txdate:String)
    }

    interface View : BlueprintContract.View {

        fun handleSuccessWithOtp(otp:String)

        fun handleLoginSuccess( res:LoginResponse)

        fun handleLoginFailure(message: String)
    }
}