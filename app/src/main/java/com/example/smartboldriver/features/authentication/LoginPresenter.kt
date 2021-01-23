package com.example.smartboldriver.features.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.auth.LoginRequest
import com.example.smartboldriver.models.User
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.utils.getErrorMessage
import com.example.smartboldriver.utils.saveUser
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

@SuppressLint("CheckResult")
class LoginPresenter : LoginContract.Presenter {

    private lateinit var mView: LoginContract.View

    private lateinit var mContext: Context

    private fun handleLoginSuccess( loginResponse: LoginResponse) {
       // mView.hideProgress()
        Log.d("response--------",loginResponse.toString())
        var user=User()
        user.deviceId = loginResponse.txnum
        user.companyCode = loginResponse.account
        user.userId = loginResponse.issuer
        user.password = loginResponse.pass
        user.token = loginResponse.user
        user.account = loginResponse.comments
        Log.d("response---2212-----",user.toString())
        saveUser(user)
        mView.handleLoginSuccess(loginResponse)
    }

    override fun login(
        account: String,
        txtype: String,
        issuer: String,
        user: String,
        password: String,
        lng: String,
        lat: String,
        idnum: String,
        comments: String,
        txdate: String
    ) {

        mView.showProgress()
        val loginRequest = LoginRequest(
            account, txtype, issuer, user, password,
            lat, lng, idnum, comments, txdate
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .login(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleLoginSuccess(it[0])
            }, {
                mView.hideProgress()
                mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: LoginContract.View) {
        mView = view
        mContext = view as Context
    }
}