package com.example.smartboldriver.features

import android.widget.TextView
import com.example.smartboldriver.basic.BlueprintContract
import com.example.smartboldriver.models.pickup.PickupResponse

class BarcodeContractor{
    interface Presenter : BlueprintContract.Presenter<View> {

        fun getPickupList(
            txtype: String,
            user: String,
            password: String,
            account: String,
            txdate: String,
            outputValue: String
        )
    }

    interface View : BlueprintContract.View {


        fun handleSuccess(res: PickupResponse)
        fun handleSuccessNew(res: PickupResponse)

        fun handleFailure(message: String)
    }
}