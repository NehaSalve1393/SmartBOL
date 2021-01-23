package com.example.smartboldriver.features

import android.content.Context
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.pickup.GetPickupListRequest
import com.example.smartboldriver.utils.saveFiles
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BarcodePresenter  :BarcodeContractor.Presenter{

    private lateinit var mView: BarcodeContractor.View

    private lateinit var mContext: Context
    override fun getPickupList(
        txtype: String,
        user: String,
        password: String,
        account: String,
        txdate: String,
        outputValue: String
    ) {

        mView.showProgress()
        val loginRequest = GetPickupListRequest(
            txtype, account, txdate, password, user, outputValue,"",""
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .getPickupList(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.handleSuccess(it[0])
                saveFiles(it[0],"")
            }, {
                mView.hideProgress()
                // mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    fun getPickupListAck(
        txtype: String,
        account: String,
        user: String,
        password: String,
        picknum: String,
        sbolnum: String,
        txdate: String,
        txnum1: String?
    ) {

        mView.showProgress()
        val loginRequest = GetPickupListRequest(
            txtype,account,txdate,password,user,picknum ,sbolnum,txnum1!!
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .getPickupList(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.handleSuccessNew(it[0])
                saveFiles(it[0],picknum)
            }, {
                mView.hideProgress()
                // mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: BarcodeContractor.View) {
        mView = view
        mContext = view as Context
    }

}