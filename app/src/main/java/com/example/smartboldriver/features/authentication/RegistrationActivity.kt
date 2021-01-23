package com.example.smartboldriver.features.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.smartboldriver.R
import com.example.smartboldriver.features.dashboard.DashBoardActivity
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.utils.encrypt_string
import com.example.smartboldriver.utils.isEmpty
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_driver_details.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() ,RegistrationContract.View{
    private lateinit var presenter: RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RegistrationPresenter()
        presenter.attach(this)

        setContentView(R.layout.activity_registration)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        var sharedpreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)

        val testflag: String = sharedpreferences!!.getString("Test", "n")!!

        if (testflag == "y") {
            // editor.putString("Test", "n")
            //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
            testlabelr.setVisibility(View.VISIBLE)
        } else {
            // editor.putString("Test", "y")
            // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
            testlabelr.setVisibility(View.GONE)
        }
        btn_register.setOnClickListener(){


            if(isValid()){
                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                val currentDate = sdf.format(Date())

                val encrpted =
                    encrypt_string(phone_id_edt_txt.text.toString(),currentDate.takeLast(4))


                val pass = encrpted.trim()

                presenter.registration("P","dreg","X","X",fullname_id_edt_txt.text.toString(),carrier_edt_txt.text.toString(),
                    phone_id_edt_txt.text.toString(),email_id_edt_txt.text.toString(),"","","",pass,currentDate,"android")


            }

        }

    }

    private fun isValid(): Boolean {
        if (fullname_id_edt_txt.isEmpty()) {
            fullname_id_edt_layout.error = "Enter full name"
            fullname_id_edt_txt.requestFocus()
            return false
        } else {
            fullname_id_edt_layout.isErrorEnabled = false
        }

        if (carrier_edt_txt.isEmpty()) {
            carrier_id_edt_layout.error = "Enter carrier "
            carrier_edt_txt.requestFocus()
            return false
        } else {
            carrier_id_edt_layout.isErrorEnabled = false
        }

        if (phone_id_edt_txt.isEmpty()) {
            phone_id_edt_layout.error = "Enter Phone number"
            phone_id_edt_txt.requestFocus()
            return false
        } else {
            phone_id_edt_layout.isErrorEnabled = false
        }


        var mStrMobile = phone_id_edt_txt.text.toString()

        val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"

        val pattern: Pattern = Pattern.compile(regex)

        if (phone_id_edt_txt.isEmpty() || ! pattern.matcher(mStrMobile).matches()) {
            phone_id_edt_layout.error = "Enter valid mobile number"
            phone_id_edt_txt.requestFocus()
            return false
        } else {
            phone_id_edt_layout.isErrorEnabled = false
        }

        if (email_id_edt_txt.isEmpty()) {
            email_id_edt_layout.error = "Enter email"
            email_id_edt_txt.requestFocus()
            return false
        } else {
            email_id_edt_layout.isErrorEnabled = false
        }

        val regexEmail =
            "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$"
        val pattern1 = Pattern.compile(regexEmail)
        val matcher = pattern1.matcher(email_id_edt_txt.text)
        if (!matcher.matches()) {
            email_id_edt_layout.error = "Enter valid email"
            email_id_edt_txt.requestFocus()
            return false
        } else {
            email_id_edt_layout.isErrorEnabled = false
        }



        return true
    }

    override fun handleRegistrationSuccess(res: LoginResponse) {
        if(res.retcode ==0) {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent);
        }
        else{
            Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
        }

    }

    override fun handleLoginFailure(message: String) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}