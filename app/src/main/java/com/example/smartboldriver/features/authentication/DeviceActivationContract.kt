package com.example.smartboldriver.features.authentication

import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.auth.LoginResponse

class DeviceActivationContract {
    interface Presenter : BlueprintContract.Presenter<View> {

        fun loginWithDevice(account:String,txtype:String,user:String,password:String,phone:String,email:String,
                         lng:String,lat:String,idnum:String,comments:String,txdate:String)
    }

    interface View : BlueprintContract.View {


        fun handleDeviceActivationSuccess( res: LoginResponse)

        fun handleLoginFailure(message: String)
    }
}