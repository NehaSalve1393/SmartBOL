package com.example.smartboldriver.features.authentication

import android.content.Context
import android.util.Log
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.auth.LoginRequest
import com.example.smartboldriver.models.User
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.auth.RegistrationRequest
import com.example.smartboldriver.utils.getErrorMessage
import com.example.smartboldriver.utils.saveUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegistrationPresenter: RegistrationContract.Presenter {

    private lateinit var mView: RegistrationContract.View

    private lateinit var mContext: Context

    private fun handleRegistrationSuccess( loginResponse: LoginResponse) {
        mView.hideProgress()
        Log.d("response--------",loginResponse.toString())
        var user= User()
        user.deviceId = loginResponse.txnum
        user.companyCode = loginResponse.account
        user.userId = loginResponse.issuer
        user.password = loginResponse.pass
        user.token = loginResponse.user
        Log.d("response---3333-----",loginResponse.toString())
        saveUser(user)
        mView.handleRegistrationSuccess(loginResponse)
    }

    override fun registration(
        account: String,
        txtype: String,
        user: String,
        password: String,
        fullname: String,
        carrier: String,
        phone: String,
        email: String,
        lng: String,
        lat: String,
        idnum: String,
        txstatus:String,
        txdate: String,
        comments:String
    ) {

        mView.showProgress()
        val loginRequest = RegistrationRequest(
             txtype, user, password,fullname,carrier,phone,email,
            lat, lng,account, idnum,txdate, txstatus,comments
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .registartion(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleRegistrationSuccess(it[0])
            }, {
                mView.hideProgress()
                mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: RegistrationContract.View) {
        mView = view
        mContext = view as Context
    }
}