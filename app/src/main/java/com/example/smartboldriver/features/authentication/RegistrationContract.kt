package com.example.smartboldriver.features.authentication

import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.auth.LoginResponse

class RegistrationContract {
    interface Presenter : BlueprintContract.Presenter<View> {

        fun registration(account:String,txtype:String,user:String,password:String,fullname:String,carrier:String,phone:String,email:String,
                  lng:String,lat:String,idnum:String,txStatus:String,txdate:String,comments:String)
    }

    interface View : BlueprintContract.View {


        fun handleRegistrationSuccess( res: LoginResponse)

        fun handleLoginFailure(message: String)
    }
}