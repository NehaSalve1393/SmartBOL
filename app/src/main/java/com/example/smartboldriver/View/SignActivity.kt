package com.example.smartboldriver.View

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.checkin.CheckOutRequest
import com.example.smartboldriver.models.pickup.GetPickupListRequest
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.*
import com.github.gcacace.signaturepad.views.SignaturePad
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_sign.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SignActivity : AppCompatActivity() {
    var signView: SignaturePad? = null
    var signature: Bitmap? = null
    var picknum: String? = null
    var location: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db  =DBHelper(this)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        picknum = intent.getStringExtra("scanCode")
        location = intent.getStringExtra("scanLocation")
        setContentView(R.layout.activity_sign)
        val btn_submit = findViewById<TextView>(R.id.btn_submit_sign)

        signView = findViewById(R.id.signature_pad1) as SignaturePad
         var tvClear = findViewById(R.id.tvClear) as TextView
        // signView.setStrokeWidth(this)
        tvClear.setOnClickListener {
          //
        }

        btn_submit_sign.setOnClickListener {
            pbSign.visibility = View.VISIBLE

            if (signView!!.getSignatureBitmap()!=null) {

                processSignature()
            }
//                val intent = Intent(this, DashBoardActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent);

            // Handler code here.

        }
    }

    fun onClearClick(v: View?) {
        signView!!.clear()
    }

    fun processSignature(

    ) {

        val signinfo = signView!!.getSignatureBitmap().toString()
        signature = signView!!.getSignatureBitmap()
        var imgString: String? = ""
        var signhash: String? = ""
        var signtime: String? = ""
        var gps = ""
        var sbolnumbers = ""
        var picknumbers = ""
        val dbhelper = DBHelper(applicationContext)
        val db: SQLiteDatabase = dbhelper.getWritableDatabase()
        var exception = ""

        if (signature != null) {
            // signature = Bitmap.createScaledBitmap(signature!!, 400, 200, false)
            signature =signView!!.getTransparentSignatureBitmap(true)
            val outputStream = ByteArrayOutputStream()
            signature!!.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            val profileImage = outputStream.toByteArray()
            imgString = Base64.encodeToString(
                profileImage,
                Base64.NO_WRAP
            )
        } else {
            imgString = ""
        }
        val hash = hashBitmap()
        try {
            val latitude = ""
            val longitude = ""
            // val sbUtils = SBUtils()
            // gps = sbUtils.getLatLong(this@SignActivity)

//            val sbolnums1 =
//                sbolnums!!.split(",".toRegex()).toTypedArray()


                signtime =
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        .format(Date())
                signhash = java.lang.Long.toString(hash)



//check network condition
            val req  = CheckOutRequest("usgn",
                getDeviceId(), getToken(),
                "P",picknum!!,"",location!!,tv_name.text.toString(), "xyz....",
                getTimestamp()!!, imgString!!,"",
                getTimestamp()!!,"","")

            ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
                .checkout(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({


                    Log.e("CHECKOUT----",it[0].toString())
                    val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                    val currentDate = sdf.format(Date())


                    val loginRequest = GetPickupListRequest(
                        "ddlv","P",currentDate, getToken(), getDeviceId(),"","",""
                    )
                    ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
                        .getPickupList(loginRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            pbSign.visibility = View.GONE
                           // mView.handlePickupListSuccess(it[0])
                            SaveInDb(it[0])
                            val loginRequest = GetPickupListRequest(
                                "ddlv","P",currentDate, getToken(), getDeviceId(),"",
                                it[0].sbolnum!!,it[0].picknum!!
                            )

                            //

                            val filename =
                                java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
                            var filestr = ""
                            try {
                                filestr = it[0]!!.imgstr!!
                            } catch (e: java.lang.Exception) {
                            }
                            var pdfpath = ""
                            if (filestr.length > 0) {

                                val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
                                val folder = File(extStorageDirectory, "/SmartBOL/Completed BOL/")
                                if (!folder.exists()) if (!folder.mkdirs()) {
                                    val i = 0
                                }
                                val f = File(folder, filename)
                                pdfpath = f.absolutePath
                                val byts: ByteArray? = getBytsFromBase64String(filestr)
                                val fos = FileOutputStream(f)
                                fos.write(byts)
                                fos.close()
                            }


                            //

                            dbhelper.addUnsignedBOL("pdf", it[0].sbolnum, it[0].bolnum, getTimestamp(), pdfpath, it[0].picknum, "n")
                            val intent: Intent = Intent(
                                this,
                                BolListActivity::class.java
                            )
                            val pdfs = ArrayList<String>()

                            pdfs.add(it[0].imgstr!!)
                            intent.putExtra("screenType", 1)

                            intent.putStringArrayListExtra("multipdfs", pdfs)
                            startActivity(intent)
                            //////////----------
                            ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
                                .getPickupList(loginRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                   // mView.handlePickupListSuccessNew(it[0])
                                   // saveFiles(it[0],"")
                                    pbLogin.visibility = View.GONE

                                    if(it.size>0){
                                        var res = it[0]
                                        SaveInDb(res)
                                    }


                                }, {
                                   // mView.hideProgress()
                                    pbSign.visibility = View.GONE
                                    Log.e("Neha---",it.getErrorMessage(applicationContext))
                                    // mView.handleLoginFailure(it.getErrorMessage(mContext))
                                })

                        }, {
                            pbSign.visibility = View.GONE
                            //mView.hideProgress()
                            // mView.handleLoginFailure(it.getErrorMessage(mContext))
                        })
                }, {
                    pbSign.visibility = View.GONE
                   // mView.hideProgress()
                    // mView.handleLoginFailure(it.getErrorMessage(mContext))
                })

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        val output = Intent()
        setResult(Activity.RESULT_OK, output)
      //  signView!!.clear()
       // finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun SaveInDb(res: PickupResponse) {

        var db  =DBHelper(this)
        // db.addUnsignedBOL("pdf", res.sbolnum, res.bolnum, getTimestamp(), pdfpath, res.picknum, "n")

        if (db.countSBOLNum(res.sbolnum!!) === 0) {
            db.addShipment(
                res.sbolnum,
                res.txdate,
                res.shipdate,
                res.srclocation,
                res.doccode,
                res.picknum,
                res.delvnum,
                res.bolnum,
                res.shipnum,
                res.shipper,
                res.shipadd1,
                res.shipadd2,
                res.shipcity,
                res.shipstate,
                res.shipzip,
                res.shipcntry,
                res.shipacc,
                res.consignee,
                res.consadd1,
                res.consadd2,
                res.conscity,
                res.consstate,
                res.conszip,
                res.conscntry,
                res.consattn,
                res.consacc,
                res.hazmat,
                res.hmcontact,
                res.spinstr,
                res.stopnum,
                res.spinstr,
                res.stopnum,
                res.spinstr,
                res.imgstr,
                res.pronum,
                res.comments,
                res.pickup
            )
        }else{
            db.updateShipment(
                res.sbolnum!!, res.txdate, res.shipdate,
                res.srclocation, res.doccode, res.picknum, res.delvnum, res.bolnum, res.shipnum, res.shipper, res.shipadd1,
                res.shipadd2, res.shipcity, res.shipstate, res.shipzip, res.shipcntry, res.shipacc, res.consignee, res.consadd1, res.consadd2,
                res.conscity, res.consstate, res.conszip, res.conscntry, res.consattn, res.consacc, res.hazmat, res.hmcontact,
                res.spinstr, res.stopnum, res.spinstr, res.stopnum, res.spinstr, res.imgstr, res.pronum, res.comments, res.pickup


            )
        }

    }
    fun hashBitmap(): Long {
        var hash: Long = 31 //or a higher prime at your choice
        for (x in 0 until signature!!.width) {
            for (y in 0 until signature!!.height) {
                hash += (signature!!.getPixel(x, y) + 31).toLong()
            }
        }
        return hash
    }

}