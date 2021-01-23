package com.example.smartboldriver.features.checkinCheckout

import android.content.Context
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.checkin.CheckInRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckInPresenter :CheckInContract.Presenter {
    private lateinit var mView: CheckInContract.View

    private lateinit var mContext: Context
    override fun checkIn(
        txtype: String,
        user: String,
        password: String,
        account: String,
        picknum: String,
        srclocation: String,
        txdate: String,
        lng: String,
        lat: String
    ) {

        mView.showProgress()
        val loginRequest = CheckInRequest(
             txtype, user, password,account,picknum,"",srclocation,"","","",txdate,
            lat, lng
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .checkin(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               if(txtype =="chkpck") {
                   mView.handleCheckInSuccess(it[0])
               }
                else{
                   mView.handleCheckOutSuccess(it[0])
               }
            }, {
                mView.hideProgress()
               // mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: CheckInContract.View) {
        mView = view
        mContext = view as Context
    }
}