package com.example.smartboldriver.features.dashboard

import android.content.Context
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.pickup.GetPickupListRequest
import com.example.smartboldriver.utils.saveFiles
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DashboardPresenter: DashboardContract.Presenter {
    private lateinit var mView: DashboardContract.View

    private lateinit var mContext: Context
    override fun getPickupList(
        txtype: String,
        user: String,
        password: String,
        account: String,
        txdate: String
    ) {
        mView.showProgress()
        val loginRequest = GetPickupListRequest(
            txtype,account,txdate,password,user,"","",""
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .getPickupList(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.handlePickupListSuccess(it[0])
                saveFiles(it[0],"")
            }, {
                mView.hideProgress()
                // mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: DashboardContract.View) {
        mView = view
        mContext = view as Context
    }

    fun getPickupListAck(txtype: String,account: String,user: String,password: String,txnum: String,
    sbolnum:String,txdate: String) {
//{"txtype":"ddlv","account":"P","user":"--","pass":"<unencrypted token>","txnum":"<picknum from the delivery received>",
// "sbolnum":"<sbolnum received>", "txdate":"<date/time>"}

        mView.showProgress()
        val loginRequest = GetPickupListRequest(
            txtype,account,txdate,password,user,"",sbolnum,txnum
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .getPickupList(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.handlePickupListSuccessNew(it[0])
                saveFiles(it[0],"")
            }, {
                mView.hideProgress()
                // mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }
}