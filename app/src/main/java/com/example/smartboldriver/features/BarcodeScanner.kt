package com.example.smartboldriver.features

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartboldriver.R
import com.example.smartboldriver.View.ActiveBOLActivity
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.text.TextRecognizer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class BarcodeScanner : AppCompatActivity(), onDataLoaded ,BarcodeContractor.View{

    var doneBtn: TextView? = null
    var closeBtn: TextView? = null
    private var cameraView: SurfaceView? = null
    private var outputValue: TextView? = null

    // private TextView bs;
    // private TextView ts;
    private var barcodeDetector: BarcodeDetector? = null
    private val textDetector: TextRecognizer? = null
    var source: CameraSource? = null
    private lateinit var presenter: BarcodePresenter
    val REQUEST_CODE = 99
    val count = 0
    var type = 0



    companion object {
        init {  var ORIENTATIONS = SparseIntArray()


            ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcode_reader)

        presenter = BarcodePresenter()
        presenter.attach(this)

         type = intent.getIntExtra("type", 0)
        val bolnum = intent.getStringExtra("bolnum")
        doneBtn = findViewById<View>(R.id.done) as TextView
        closeBtn = findViewById<View>(R.id.close) as TextView
        outputValue = findViewById<View>(R.id.outputValue) as TextView
        // bs = (TextView) findViewById(R.id.bs);
        //ts = (TextView) findViewById(R.id.ts);
        cameraView = findViewById<View>(R.id.texture) as SurfaceView
        assert(cameraView != null)
        barcodeScanner()
        closeBtn!!.setOnClickListener { finish() }
        doneBtn!!.setOnClickListener {
            val output = outputValue!!.text.toString()
            if (output.length > 0) {
//                if (type == 1) {
//                    Popup.setShipmentNumber(output)
//                    finish()
//                }
//                if (type == 2) {
//                    Popup.setTrailerNumber(output)
//                    finish()
//                }
//                if (type == 3) {
//                    Popup.setBolNumber(output)
//                    finish()
//                }
                if (type == 100) {
                    barcodeDetector!!.release()
                    source!!.stop()
                    val t: Thread = object : Thread() {
                        override fun run() {
                            try {
                               // val clsUplTrans = ClsUplTrans(this@BarcodeReader)
                               // uploadPronum(this@BarcodeReader, bolnum, output)


                               // add Api call
                                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                                val currentDate = sdf.format(Date())
                                var op = outputValue!!.text.substring(2)

                              //  if()
                                presenter.getPickupList(
                                    "ddlv",
                                    getDeviceId(), getToken(),
                                    "P", currentDate, op
                                )
                            } catch (e: Exception) {
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
                }
                else if(type == 1 || type == 2){
                    barcodeDetector!!.release()
                    source!!.stop()
                    returndata()
                }
            }
            finish()
        }


    }
    fun returndata(){
        var op = outputValue!!.text

        val intent = Intent()
        intent.putExtra("sbolnum", op)

        intent.putExtra("type", type)

        setResult(RESULT_OK, intent)
        finish()

    }

    fun barcodeScanner() {
        cameraView = findViewById<View>(R.id.texture) as SurfaceView
        assert(cameraView != null)
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        source = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(5.0f)
            .build()
        cameraView!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) !== PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@BarcodeScanner,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                        return
                    }
                    source!!.start(cameraView!!.holder)
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
                    source!!.start(cameraView!!.holder)
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
                Log.e("neha.....nbarcode", barcodes.toString())
                if (barcodes.size() != 0) {
                    outputValue!!.post { outputValue!!.text = barcodes.valueAt(0).displayValue }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        // source.release();
        barcodeDetector!!.release()
    }

    override fun dataLoaded(token: String?) {

    }

    override fun pdfLoaded(picknum: String?) {

        val mydb = DBHelper(applicationContext)

        val data: HashMap<*, *> = mydb.getBarcodePDF(picknum!!)!!
        if (data.size != 0) {
            val sbols =
                data["sbols"] as ArrayList<String>?
            val bols =
                data["bols"] as ArrayList<String>?
            val picks =
                data["picks"] as ArrayList<String>?
            val pdfs =
                data["pdfs"] as ArrayList<String>?
            val intent =
                Intent(applicationContext, ActiveBOLActivity::class.java)
            intent.putStringArrayListExtra("multisbol", sbols)
            intent.putStringArrayListExtra("multibol", bols)
            intent.putStringArrayListExtra("multipdfs", pdfs)
            intent.putStringArrayListExtra("multipick", picks)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        } else {
//            Intent intent = new Intent(BarcodeReader.this, BarcodeReader.class);
//            intent.putExtra("type", 100);
//            startActivity(intent);
        }
    }

    override fun handleSuccess(res: PickupResponse) {
        Log.e("barcode-----", res.toString())

        Log.d("Neha.............", res.toString())
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())

        Log.d("getPickupListAck..", res.sbolnum.toString())



       // for (x in 0..5) {
            presenter.getPickupListAck(
                "ddlv", "p",
                getDeviceId(), getToken(), res.picknum!!, res.sbolnum!!,
                currentDate, res.picknum
            )
      //  }
        var db  =DBHelper(this)
       // val mydb = DBHelper(context)
     //   mydb.addUnsignedBOL("pdf", res.sbolnum, res.bolnum, res.downloaddate, res.pdfpath, res.picknum, "n")
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
      //  saveFiles(res)
        val filename =
            java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
//        val jsonArray = JSONArray(jsonStr)
//        val jsonObject = jsonArray.getJSONObject(0)
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
        db.addUnsignedBOL("pdf", res.sbolnum, res.bolnum, getTimestamp(), pdfpath, res.picknum, "n")
    }

    override fun handleSuccessNew(res: PickupResponse) {

        Log.e("barcode  Neha-----", res.toString())
        Log.e("Neha-Scan Delivery", count.toString())


        val filename =
            java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
//        val jsonArray = JSONArray(jsonStr)
//        val jsonObject = jsonArray.getJSONObject(0)
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
        var db  =DBHelper(this)
        db.addUnsignedBOL("pdf", res.sbolnum, res.bolnum, getTimestamp(), pdfpath, res.picknum, "n")
        if (db.countSBOLNum(res.sbolnum!!) === 0) {
            db.addShipment(
                res.sbolnum, res.txdate, res.shipdate,
                res.srclocation, res.doccode, res.picknum, res.delvnum, res.bolnum, res.shipnum, res.shipper, res.shipadd1,
                res.shipadd2, res.shipcity, res.shipstate, res.shipzip, res.shipcntry, res.shipacc, res.consignee, res.consadd1, res.consadd2,
                res.conscity, res.consstate, res.conszip, res.conscntry, res.consattn, res.consacc, res.hazmat, res.hmcontact,
                res.spinstr, res.stopnum, res.spinstr, res.stopnum, res.spinstr, res.imgstr, res.pronum, res.comments, res.pickup
            )
        }else{

          //  Toast.makeText(this,"Already scanned",Toast.LENGTH_LONG).show()
            db.updateShipment(
                res.sbolnum!!, res.txdate, res.shipdate,
                res.srclocation, res.doccode, res.picknum, res.delvnum, res.bolnum, res.shipnum, res.shipper, res.shipadd1,
                res.shipadd2, res.shipcity, res.shipstate, res.shipzip, res.shipcntry, res.shipacc, res.consignee, res.consadd1, res.consadd2,
                res.conscity, res.consstate, res.conszip, res.conscntry, res.consattn, res.consacc, res.hazmat, res.hmcontact,
                res.spinstr, res.stopnum, res.spinstr, res.stopnum, res.spinstr, res.imgstr, res.pronum, res.comments, res.pickup


            )
        }

      // saveFiles(res)

        finish()
    }

    override fun handleFailure(message: String) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}