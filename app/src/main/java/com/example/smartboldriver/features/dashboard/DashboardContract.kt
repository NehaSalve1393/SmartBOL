package com.example.smartboldriver.features.dashboard

import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.pickup.PickupResponse

class DashboardContract {
    interface Presenter : BlueprintContract.Presenter<View> {

        fun getPickupList(txtype:String,user:String,password:String,account:String,
                   txdate:String)
    }

    interface View : BlueprintContract.View {



        fun handlePickupListSuccess( res: PickupResponse)
        fun handlePickupListSuccessNew( res: PickupResponse)

        fun handlePickupListFailure(message: String)
    }
}