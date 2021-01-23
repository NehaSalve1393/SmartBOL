package com.example.smartboldriver.features.checkinCheckout

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartboldriver.R
import com.example.smartboldriver.View.ActiveBOLActivity
import com.example.smartboldriver.View.BolListActivity
import com.example.smartboldriver.View.QRScannerActivity
import com.example.smartboldriver.View.SignActivity
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_checkin.pbLogin
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CheckinActivity : AppCompatActivity(),CheckInContract.View {
    private lateinit var presenter: CheckInPresenter

    private var cameraView: SurfaceView? = null
    private var barcodeDetector: BarcodeDetector? = null
    var source: CameraSource? = null
    var locationCode:String? = null
    var pickupCode:String? = null
    var sessionId :String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        presenter = CheckInPresenter()
        presenter.attach(this)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        val login_txt = findViewById<TextView>(R.id.login_txt)
         sessionId = intent.getStringExtra("EXTRA_SESSION_ID")!!
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  "+currentDate)
        Log.e("Session iD",sessionId.toString())
        Log.e("currentDate",currentDate.toString())
      var   sharedpreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)

        val testflag: String = sharedpreferences!!.getString("Test", "n")!!

        if (testflag == "y") {
           // editor.putString("Test", "n")
            //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
            test_env_txt.setVisibility(View.GONE)
        } else {
           // editor.putString("Test", "y")
            // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
            test_env_txt.setVisibility(View.VISIBLE)
        }





            login_txt.setOnClickListener {
                if(isValid()) {

                pbLogin.visibility = View.VISIBLE
                locationCode = location_code_edt_txt.text.toString()
                pickupCode = pickup_code_edt_txt.text.toString()
                if (sessionId.equals("checkin")) {
                    // Toast.makeText(this,"Dock:120",Toast.LENGTH_LONG).show()
                    presenter.checkIn(
                        "chkpck",
                        getDeviceId(),
                        getToken(),
                        "P",
                        pickup_code_edt_txt.text.toString(),
                        location_code_edt_txt.text.toString(),
                        currentDate,
                        "",
                        ""
                    )
                } else {

                    presenter.checkIn(
                        "dlpck",
                        getDeviceId(),
                        getToken(),
                        "P",
                        pickup_code_edt_txt.text.toString(),
                        location_code_edt_txt.text.toString(),
                        currentDate,
                        "",
                        ""
                    )

                    /* val intent = Intent(this, SignActivity::class.java)
                    startActivity(intent);*/
                }
                // Handler code here.

            }
        }
        btn_scan_code.setOnClickListener(){
            val intent = Intent(this, QRScannerActivity::class.java)
            startActivity(intent);
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun isValid(): Boolean {
        if (location_code_edt_txt.isEmpty()) {
            location_code_edt_layout.error = "Enter Location code "
            location_code_edt_txt.requestFocus()
            return false
        } else {
            location_code_edt_layout.isErrorEnabled = false
        }
        if (pickup_code_edt_txt.isEmpty()) {
            pickup_code_edt_layout.error = "Enter Pickup code"
            pickup_code_edt_txt.requestFocus()
            return false
        } else {
            pickup_code_edt_layout.isErrorEnabled = false
        }






        return true
    }
    fun barcodeScanner() {
        cameraView = findViewById(R.id.texture) as SurfaceView
        assert(cameraView != null)
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        source = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(5.0f)
            .build()
        cameraView!!.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) !== PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@CheckinActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                        return
                    }
                    source!!.start(cameraView!!.getHolder())
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                try {
                    source!!.start(cameraView!!.getHolder())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                source!!.stop()
            }
        })
        barcodeDetector!!.setProcessor(object :
            Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    Log.e(
                        "-------",
                        "----barcodes-----" + barcodes.valueAt(0).displayValue
                    )
                    loginFromBarcode(barcodes.valueAt(0).displayValue)
                    //                    outputValue.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            outputValue.setText(barcodes.valueAt(0).displayValue);
//                        }
//                    });
                }
            }
        })
    }


    private fun loginFromBarcode(displayValue: String) {

        Log.e("Checkin",displayValue)
       /* if (displayValue.contains("_X")) {
            val scancode = displayValue.replace("_X", "")
            if (scancode.contains("_")) {
                val idPass =
                    scancode.split("_".toRegex()).toTypedArray()
                com.smartbol.dcapp.Login.NAME = idPass[0]
                com.smartbol.dcapp.Login.ORIGPASS = idPass[1]
                Log.e(
                    "-------",
                    "NAME " + com.smartbol.dcapp.Login.NAME + "ORIGPASS " + com.smartbol.dcapp.Login.ORIGPASS
                )
                if (com.smartbol.dcapp.Login.NAME.length > 0 && com.smartbol.dcapp.Login.ORIGPASS.length > 0) {
                    showDialog("", this@Login)
                    com.smartbol.dcapp.Login.PASS =
                        encrypt_string(com.smartbol.dcapp.Login.ORIGPASS)
                    val thread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                loginsuccess = authenticate(
                                    com.smartbol.dcapp.Login.NAME,
                                    com.smartbol.dcapp.Login.PASS,
                                    com.smartbol.dcapp.Login.WHCODE
                                )
                            } catch (e: Exception) {
                                val x = e.message
                            }
                        }
                    }
                    thread.start()
                    try {
                        thread.join()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    Log.e("-------", "loginsuccess $loginsuccess")
                    if (loginsuccess) {
                        val editor: SharedPreferences.Editor =
                            sharedpreferences.edit()
                        //                        if (checkBox.isChecked()) {
                        if (switchRem.isChecked()) {
                            editor.putString("NAME", com.smartbol.dcapp.Login.NAME)
                            editor.putString("ORIGPASS", com.smartbol.dcapp.Login.ORIGPASS)
                            editor.putString("PASS", com.smartbol.dcapp.Login.PASS)
                            editor.putString("WHCODE", com.smartbol.dcapp.Login.WHCODE)
                        }
                        editor.commit()
                        val t: Thread = object : Thread() {
                            override fun run() {
                                try {
                                    val dbHelper = DBHelper(this@Login)
                                    dbHelper.clearDatabase()
                                    val clsUplTrans = ClsUplTrans(this@Login)
                                    DownloadShipments(null)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    val x = e.message
                                }
                            }
                        }
                        t.start()
                        try {
                            t.join()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

//                        barcodeDetector.release();
//                        source.stop();
                        dismissDialog()
                        val intent = Intent(
                            this@Login,
                            ListShipments::class.java
                        )
                        //                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent)

//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                dismissDialog();
//                                Intent intent = new Intent(Login.this, ListShipments.class);
////                        Intent intent = new Intent(Login.this, Menu.class);
//                                startActivity(intent);
//                            }
//                        }, 1000);
                    } else {
                        dismissDialog()
                        //                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }*/
    }


    override fun handleCheckInSuccess(res: PickupResponse) {
        pbLogin.visibility = View.GONE

        if(res.retcode==0 && res.txstatus!! >= 200.toString()){
            Toast.makeText(this,"This pickup has already been checked-in",Toast.LENGTH_LONG).show()
            return;
        }

        if(res.retcode !=0 && res.retcode!! > 200){
            Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()

        }
        if(res.retcode == 0 && sessionId.equals("checkin") && res.txstatus!!< 200.toString()){
            val intent = Intent(this, DriverDetailsActivity::class.java)

            intent.putExtra("location_code",locationCode );
            intent.putExtra("pickup_code",pickupCode );
            startActivity(intent);


        }
        else{
            Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
        }

    }

    override fun handleCheckOutSuccess(res: PickupResponse) {
        pbLogin.visibility = View.GONE
        if(res!=null){
            if(res.retcode == 9){
                Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
            }
            else if(res.retcode ==10){
                Toast.makeText(this,"No Preview available",Toast.LENGTH_LONG).show()
            }
            else if(res.retcode ==7){
                Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
            }
            else if(res.retcode ==1){
                Toast.makeText(this,res.txstatus,Toast.LENGTH_LONG).show()
            }
            else{
                if(res.retcode == 0){
                    val intent: Intent = Intent(
                        this,
                        BolListActivity::class.java
                    )

                  //

                    val filename =
                        java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
                    var filestr = ""
                    try {
                        filestr = res.imgstr!!
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
                    intent.putExtra("scanCode",pickup_code_edt_txt.text.toString() )
                    intent.putExtra("screen","checkout" )
                    intent.putExtra("scanLocation", location_code_edt_txt.text.toString())

                    val pdfs = ArrayList<String>()

                    pdfs.add(res.imgstr!!)

                    intent.putStringArrayListExtra("multipdfs", pdfs)
                    intent.putExtra("path", pdfpath)
                    intent.putExtra("screenType", 10)
                    intent.putExtra("consinee", res.consignee)
                    intent.putExtra("city", res.conscity+" "+res.consstate)
                    intent.putExtra("unit", res.units)
                    intent.putExtra("weight", res.weight)


                    startActivity(intent)
                }

            }
        }

    }

    override fun handleCheckInFailure(message: String) {
        pbLogin.visibility = View.GONE

    }

    override fun handleCheckOutFailure(message: String) {
        pbLogin.visibility = View.GONE

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}


