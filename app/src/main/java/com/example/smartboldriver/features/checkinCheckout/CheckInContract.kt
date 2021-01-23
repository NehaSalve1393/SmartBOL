package com.example.smartboldriver.features.checkinCheckout

import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.pickup.PickupResponse

class CheckInContract {
    interface Presenter : BlueprintContract.Presenter<View> {

        fun checkIn(txtype:String,user:String,password:String,account:String,
                    picknum:String,srclocation:String,txdate:String,
                  lng:String,lat:String)
    }

    interface View : BlueprintContract.View {



        fun handleCheckInSuccess( res: PickupResponse)
        fun handleCheckOutSuccess( res: PickupResponse)

        fun handleCheckInFailure(message: String)
        fun handleCheckOutFailure(message: String)
    }
}