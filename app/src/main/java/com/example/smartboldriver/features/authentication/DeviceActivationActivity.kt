package com.example.smartboldriver.features.authentication

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartboldriver.R
import com.example.smartboldriver.features.dashboard.DashBoardActivity
import com.example.smartboldriver.models.auth.LoginResponse
import com.example.smartboldriver.utils.encrypt_string
import com.example.smartboldriver.utils.getDeviceId
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_device_activation.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DeviceActivationActivity : AppCompatActivity(),DeviceActivationContract.View {
    private lateinit var presenter: DeviceActivationPresenter
    private var cameraView: SurfaceView? = null
    private var barcodeDetector: BarcodeDetector? = null
    var source: CameraSource? = null
    var NAME: String? = null
    var ORIGPASS: String? = null
    var PASS: String? = null
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = DeviceActivationPresenter()
        presenter.attach(this)

        setContentView(R.layout.activity_device_activation)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

         cameraView = findViewById(R.id.img_vieww) as SurfaceView
        assert(cameraView != null)
        barcodeScanner()

        btn_device_activation.setOnClickListener(){


            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
            val currentDate = sdf.format(Date())
            if(activation_code_edt_txt.text!!.isNotEmpty() &&device_id_edt_txt.text!!.isNotEmpty() ) {

                var substr = activation_code_edt_txt.text.toString().substring(2, 6)
                //  var substr = "activation".substring(2,4)
                Log.d("Substring---", substr)

                val encrpted =
                    encrypt_string(activation_code_edt_txt.text.toString(), substr)


                val pass = encrpted.trim()

                presenter.loginWithDevice(
                    "P", "auth", device_id_edt_txt.text.toString(),
                    pass, "", "", "", "", "", "android", currentDate
                )
            }
            else{
                Toast.makeText(this,"Fill the details",Toast.LENGTH_LONG).show()
            }


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun arePermissionsEnabled(): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestMultiplePermissions() {
        val remainingPermissions: MutableList<String> =
            ArrayList()
        for (permission in permissions) {
            if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission)
            }
        }
        requestPermissions(remainingPermissions.toTypedArray(), 101)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i]!!)) {
                        AlertDialog.Builder(this)
                            .setMessage("App will not work unless all permissions are granted")
                            .setPositiveButton(
                                "Ask again",
                                DialogInterface.OnClickListener { dialog, id -> requestMultiplePermissions() })
                            .setNegativeButton(
                                "Ignore",
                                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
                            .create()
                            .show()
                    }
                    return
                }
            }
            barcodeScanner()
            //all is good, continue flow
        }
    }


    fun barcodeScanner() {
        cameraView = findViewById(R.id.img_vieww) as SurfaceView
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
                            this@DeviceActivationActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                        return
                    }
                    source?.start(cameraView!!.getHolder())
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
                source?.stop()
            }
        })
        barcodeDetector?.setProcessor(object :
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

    private fun loginFromBarcode(displayValue: String?) {
        if (displayValue!!.contains("_X")) {
            val scancode = displayValue!!.replace("_X", "")
            if (scancode.contains("_")) {
                val idPass =
                    scancode.split("_".toRegex()).toTypedArray()
              NAME = idPass[0]
               ORIGPASS = idPass[1]
                Log.e(
                    "-------",
                    "NAME " + NAME + "ORIGPASS " +ORIGPASS
                )
                if (NAME?.length!! > 0 && ORIGPASS?.length!! > 0) {
                  //  showDialog("", this@Login)
                    PASS

                    val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                    val currentDate = sdf.format(Date())
                    System.out.println(" C DATE is  "+currentDate)
                    val encrptedPass =
                        //encrypt_string(ORIGPASS!!,currentDate.takeLast(4))
                        encrypt_string(ORIGPASS!!, ORIGPASS!!.substring(2,6))


                    PASS  = encrptedPass.trim()
                    presenter.loginWithDevice("P","auth", NAME!!, PASS!!,"","","",
                        "","","android",currentDate)



                }
            }
        }

    }


    override fun handleDeviceActivationSuccess(res: LoginResponse) {
        source?.stop()
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent);

    }

    override fun handleLoginFailure(message: String) {
        source?.stop()
       // Toast.makeText(this,message,Toast.LENGTH_LONG).show()

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}