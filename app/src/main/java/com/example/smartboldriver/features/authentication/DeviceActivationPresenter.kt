package com.example.smartboldriver.features.authentication

import android.content.Context
import android.util.Log
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.User
import com.example.smartboldriver.models.auth.DeviceActivationRequest
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.auth.RegistrationRequest
import com.example.smartboldriver.utils.getErrorMessage
import com.example.smartboldriver.utils.saveUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DeviceActivationPresenter: DeviceActivationContract.Presenter {

    private lateinit var mView: DeviceActivationContract.View

    private lateinit var mContext: Context

    private fun handleDeviceActivationSuccess( loginResponse: LoginResponse) {
        mView.hideProgress()
        Log.d("response--------",loginResponse.toString())
        if(loginResponse.retcode == 0) {
            var user = User()
            user.deviceId = loginResponse.txnum
            user.companyCode = loginResponse.account
            user.userId = loginResponse.issuer
            user.password = loginResponse.pass
            user.token = loginResponse.user
            user.account = loginResponse.comments
            Log.d("response---3333-----", loginResponse.toString())
            saveUser(user)
            mView.handleDeviceActivationSuccess(loginResponse)
        }
        else{
            mView.handleLoginFailure("Failed")
        }
    }

    override fun loginWithDevice(
        account: String,
        txtype: String,
        user: String,
        password: String,
        phone: String,
        email: String,
        lng: String,
        lat: String,
        idnum: String,
        comments:String,
        txdate: String
    ) {

        mView.showProgress()
        val loginRequest = DeviceActivationRequest(account,
            txtype, user, password,phone,email,
            lat, lng, idnum,comments,txdate
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .deviceActivation(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleDeviceActivationSuccess(it[0])
            }, {
                mView.hideProgress()
                mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }

    override fun attach(view: DeviceActivationContract.View) {
        mView = view
        mContext = view as Context
    }
}