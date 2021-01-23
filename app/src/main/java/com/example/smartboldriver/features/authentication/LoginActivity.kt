package com.example.smartboldriver.features.authentication

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartboldriver.R
import com.example.smartboldriver.api.SmartBOL24Retrofit
import com.example.smartboldriver.features.dashboard.DashBoardActivity
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.utils.DBHelper
import com.example.smartboldriver.utils.encrypt_string
import com.example.smartboldriver.utils.getManifestPermissions
import com.example.smartboldriver.utils.hasPermissions
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.pbLogin
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() ,LoginContract.View{
    private val logo: ImageView? = null
    private var isLongPress = false
    private val longClickDuration = 3000
    var sharedpreferences: SharedPreferences? = null
   // val tvLogin,tvRegistration,tvDeviceActivation;
   private lateinit var presenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenter()
        presenter.attach(this)
        handlePermissions()

        setContentView(R.layout.activity_login)
       if (getSupportActionBar() != null) {
           getSupportActionBar()?.hide();
       }
        sharedpreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)
        val editor = sharedpreferences!!.edit()
        if (!sharedpreferences!!.contains("Test")) {
            editor.putString("Test", "n")
        }


        val testflag: String = sharedpreferences!!.getString("Test", "n")!!

        if (testflag == "y") {
            // editor.putString("Test", "n")
            //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
            testlabel.setVisibility(View.VISIBLE)
        } else {
            // editor.putString("Test", "y")
            // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
            testlabel.setVisibility(View.GONE)
        }


        editor.commit()

        img_logo!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                isLongPress = true
                val handler = Handler()
                handler.postDelayed({
                    if (isLongPress) {
                        val vibrator =
                            this.getSystemService(VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(100)
                        var alert = ""
                        val testflag: String = sharedpreferences!!.getString("Test", "n")!!
                        if (sharedpreferences!!.contains("Test")) {
                            alert = if (testflag == "y") {
                                "Changing from test environment to production environment will erase all app data"
                            } else {
                                "Changing from production environment to test environment will erase all app data"
                            }
                        }
                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                val editor: SharedPreferences.Editor =
                                    sharedpreferences!!.edit()
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        if (testflag == "y") {
                                            editor.putString("Test", "n")
                                         //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
                                            testlabel.setVisibility(View.GONE)
                                        } else {
                                            editor.putString("Test", "y")
                                           // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
                                            testlabel.setVisibility(View.VISIBLE)
                                        }
                                        editor.commit()
                                        val db = DBHelper(this)
                                        db.getWritableDatabase().execSQL("delete from gps")
                                        db.getWritableDatabase().execSQL("delete from images")
                                        db.getWritableDatabase().execSQL("delete from shipments")
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }
                                }
                            }
                        val builder: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(this)
                        builder.setTitle("WARNING")
                            .setMessage(alert).setPositiveButton("Continue", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show()
                    }
                }, longClickDuration.toLong())
            } else if (event.action == MotionEvent.ACTION_UP) {
                isLongPress = false
            }
            true
        }


        val tvLogin = findViewById<TextView>(R.id.btn_login)
       val tvRegistration = findViewById<TextView>(R.id.txt_registration)
       val tvDeviceActivation = findViewById<TextView>(R.id.txt_device_activation)
       tvLogin.setOnClickListener {
            // Handler code here.


           pbLogin.visibility = View.VISIBLE
           val sdf = SimpleDateFormat("yyyyMMddHHmmss")
           val currentDate = sdf.format(Date())
           System.out.println(" C DATE is  " + currentDate)
           val encrptedPass =
               encrypt_string(activation_code_edt_txt.text.toString(), currentDate.takeLast(4))


           val pass = encrptedPass.trim()
           System.out.println(" C DATE is  " + pass)

         //  {"account":"P","txtype":"ausr","issuer":"--","user":"--","pass":"--","lng":"--","lat":"--","idnum":"<<FCM_id>>", "comments":"ios","txdate":"<date/time>"}
           presenter.login(
               "P", "ausr", device_id_edt_txt.text.toString(), "X",
               pass,
               "", "", "", "Android", currentDate
           )

        }
       tvRegistration.setOnClickListener {
           // Handler code here.
           val intent = Intent(this, RegistrationActivity::class.java)
           startActivity(intent);
       }
       tvDeviceActivation.setOnClickListener {
           // Handler code here.
           val intent = Intent(this, DeviceActivationActivity::class.java)
           startActivity(intent);
       }

    }


    private fun handlePermissions() {
        val permissions = getManifestPermissions()

        if (hasPermissions(permissions)) {
        } else {
            ActivityCompat.requestPermissions(this, permissions as Array<out String>, 101)
        }
    }

    override fun handleSuccessWithOtp(otp: String) {
       // Toast.makeText(this,"handleSuccessWithOtp",Toast.LENGTH_LONG).show()
    }

    override fun handleLoginSuccess(res: LoginResponse) {
        pbLogin.visibility = View.GONE
        if(res.retcode==0){
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent);
            Log.d("Neha.NEHANEHA..", res.toString())

        }
        else{
             Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
        }

      //  Toast.makeText(this,"handleLoginSuccess",Toast.LENGTH_LONG).show()
    }



    override fun handleLoginFailure(message: String) {
        pbLogin.visibility = View.GONE
        Toast.makeText(this, "handleLoginFailure", Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
      //  Toast.makeText(this,"showProgress",Toast.LENGTH_LONG).show()
    }

    override fun hideProgress() {
      //  Toast.makeText(this,"hideProgress",Toast.LENGTH_LONG).show()
    }
}