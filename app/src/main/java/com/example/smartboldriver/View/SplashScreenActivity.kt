package com.example.smartboldriver.View

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.features.authentication.LoginActivity
import com.example.smartboldriver.features.dashboard.DashBoardActivity
import com.example.smartboldriver.models.auth.ReAuthRequest
import com.example.smartboldriver.utils.DBHelper
import com.example.smartboldriver.utils.getCompanyAccountCode
import com.example.smartboldriver.utils.getDeviceId
import com.example.smartboldriver.utils.getUserId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var deviceId = getDeviceId()

//apicallReauth()
        Handler().postDelayed(
            {
                if (deviceId.isEmpty()) {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                } else {

                    val sharedpreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)
                    if (sharedpreferences.contains("Delete")) {
                        val numdays = sharedpreferences.getString("Delete", "")
                        if (numdays!!.length > 0) {
                            val deletedays = -1 * numdays.toInt()
                            AsyncTask.execute {
                                val currentDate = Date()
                                val c = Calendar.getInstance()
                                c.time = currentDate
                                c.add(Calendar.DAY_OF_MONTH, deletedays)
                                val deletedate = c.time
                                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                                val olddate = sdf.format(deletedate)
                                val dbhelper = DBHelper(this)
                                val res: String = dbhelper.deleteShipments(olddate)!!
                            }
                        }
                    }


                    val i = Intent(this, DashBoardActivity::class.java)
                    startActivity(i)
                }

                finish()
            }, SPLASH_TIME_OUT
        )
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    fun apicallReauth(){

        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        //{"txtype":"reauth","account":"P","user":"---","issuer":"--","pass":"---", "lng":"---","lat":"---", "txdate":"<date/time>"}
        val loginRequest = ReAuthRequest(
            "reauth", "P",
            getDeviceId(), getUserId(), getCompanyAccountCode(), "", "",
            currentDate
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .reauth(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // handleLoginSuccess(it[0])
            }, {
//                mView.hideProgress()
//                mView.handleLoginFailure(it.getErrorMessage(mContext))
            })
    }
}