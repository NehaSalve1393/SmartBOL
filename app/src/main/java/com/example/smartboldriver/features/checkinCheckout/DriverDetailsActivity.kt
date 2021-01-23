package com.example.smartboldriver.features.checkinCheckout

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.features.dashboard.DashBoardActivity
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.getDeviceId
import com.example.smartboldriver.utils.getToken
import com.example.smartboldriver.utils.isEmpty
import com.example.smartboldriver.utils.saveDriverDetails
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_driver_details.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class DriverDetailsActivity : AppCompatActivity() ,DriverDetailsContractor.View{

    private lateinit var presenter: DriverDetailsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        presenter = DriverDetailsPresenter()
        presenter.attach(this)
        val locationCode = intent.getStringExtra("location_code")
        val pickupCode = intent.getStringExtra("pickup_code")
        btn_submit.setOnClickListener{

            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
            val currentDate = sdf.format(Date())
            System.out.println(" C DATE is  " + currentDate)
           /* {"txtype":"chkin","user":"--","pass":"<unencrypted token>","account":"P","txstatus":"200",
                "picknum":"<pickup>", "srclocation":"<location code>", "fullname":"<from profile>","phone":"<from profile>",
                "carrier":"<from profile>","txdate":"<date/time>","lng":"---","lat":"---"}

*/
            if(isValid()) {
                pbDriverDetails.visibility = View.VISIBLE


                presenter.checkIn(
                    "chkin",
                    getDeviceId(),
                    getToken(),
                    "P",
                    "200",
                    pickupCode.toString(),
                    locationCode.toString(),
                    currentDate.toString(),
                    edt_name.text.toString(),
                    edt_driver_phone.text.toString(),
                    edt_com_name.text.toString(),
                    "",
                    ""
                )

            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, DashBoardActivity::class.java)


        startActivity(intent);

    }

    private fun isValid(): Boolean {
        var mStrMobile = edt_driver_phone.text.toString()

        val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"

        val pattern: Pattern = Pattern.compile(regex)

        if (edt_driver_phone.isEmpty() || ! pattern.matcher(mStrMobile).matches()) {
            driver_phonr_edt_layout.error = "Enter valid mobile number"
            edt_driver_phone.requestFocus()
            return false
        } else {
            driver_phonr_edt_layout.isErrorEnabled = false
        }






        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        onBackPressed()
    }

    override fun handleCheckInSuccess(res: PickupResponse) {
        pbDriverDetails.visibility = View.GONE
        //Toast.makeText(this,"DOCK :${res.dock}",Toast.LENGTH_LONG).show()
        tvDock.text = "DOCK :"+" "+res.dock
        saveDriverDetails(edt_com_name.text.toString())

    }

    override fun handleCheckInFailure(message: String) {
        pbDriverDetails.visibility = View.GONE

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}