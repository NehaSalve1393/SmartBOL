package com.example.smartboldriver.features.dashboard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.View.ActiveBOLActivity
import com.example.smartboldriver.View.SettingsActivity
import com.example.smartboldriver.features.BarcodeScanner
import com.example.smartboldriver.features.checkinCheckout.CheckinActivity
import com.example.smartboldriver.features.shipments.DeliveryListActivity
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.*
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DashBoardActivity : AppCompatActivity(),DashboardContract.View {
    private lateinit var presenter: DashboardPresenter
    val REQUEST_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_dashboard_layout)




        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(
                    Intent(
                        this,
                        BGService::class.java
                    )
                )
            } else {
                // BackgroundUploadService.scheduleJob(this);
                // ContextCompat.startForegroundService(MainMenu.this, new Intent(MainMenu.this, BackgroundService.class));
            }
        } catch (e: java.lang.Exception) {
            val s = e.toString()
        }


        presenter = DashboardPresenter()
        presenter.attach(this)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
       val img_setting = findViewById<LinearLayout>(R.id.sett_img)

        val ll_checkin = findViewById<LinearLayout>(R.id.ll_checkin)
        val ll_checkout = findViewById<LinearLayout>(R.id.ll_checkout)
        val ll_capture_pod = findViewById<LinearLayout>(R.id.ll_capture_pod)
        val ll_active_bol = findViewById<LinearLayout>(R.id.ll_active_bol)
        val ll_delivery = findViewById<LinearLayout>(R.id.ll_delivery)
        val ll_scan_delivery = findViewById<LinearLayout>(R.id.ll_scan_delivery)

//        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
//        val currentDate = sdf.format(Date())
//        presenter.getPickupList(
//            "ddlv",
//            getDeviceId(), getToken(),
//            "P", currentDate
//        )


        img_setting.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent);
        }
        ll_checkin.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, CheckinActivity::class.java)
            intent.putExtra("EXTRA_SESSION_ID", "checkin");
            startActivity(intent);
        }
        ll_checkout.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, CheckinActivity::class.java)
            intent.putExtra("EXTRA_SESSION_ID", "checkout");
            startActivity(intent);
        }

        ll_active_bol.setOnClickListener {
            // Handler code here.
           // val intent = Intent(this, ConsigneeInfoActivity::class.java)
//            val intent = Intent(this, ActiveBOLActivity::class.java)
//            intent.putExtra("home","dashboard")
//            startActivity(intent);

            val picknums = ArrayList<String>()
            val sbolnums = ArrayList<String>()
            val bolnums = ArrayList<String>()
            val pdfs = ArrayList<String>()

            val mydb = DBHelper(this)
            try {
                val data = mydb.getActivePDF()
                picknums.addAll(data!!["picks"] as Collection<String>)
                sbolnums.addAll(data["sbols"] as Collection<String>)
                bolnums.addAll(data["bols"] as Collection<String>)
                pdfs.addAll(data["pdfs"] as Collection<String>)
            } catch (e: Exception) {
                val s = e.toString()
            }
            val intent: Intent = Intent(
                this,
                ActiveBOLActivity::class.java
            )
            intent.putStringArrayListExtra("multipick", picknums)
            intent.putStringArrayListExtra("multisbol", sbolnums)
            intent.putStringArrayListExtra("multibol", bolnums)
            intent.putStringArrayListExtra("multipdfs", pdfs)
            startActivity(intent)
        }
    ll_delivery.setOnClickListener {
        // Handler code here.
       // val intent = Intent(this, DetailsActivity::class.java)

        val intent = Intent(this, DeliveryListActivity::class.java)
        startActivity(intent);
    }
        ll_scan_delivery.setOnClickListener {

//            val intent = Intent(this, QRScannerActivity::class.java)
//            startActivity(intent);

                val intent =
                    Intent(this, BarcodeScanner::class.java)
                intent.putExtra("type", 100)
                startActivity(intent)

        }
        ll_capture_pod.setOnClickListener {
            // Handler code here.
            // val intent = Intent(this, DetailsActivity::class.java)
           // val intent = Intent(this, CapturePODFormActivity::class.java)
          //  val intent = Intent(this, CapturePODFormActivity::class.java)

            val preference = ScanConstants.OPEN_CAMERA
            val intent =
                Intent(this, ScanActivity::class.java)
            intent.putExtra("sbol", "")
            intent.putExtra("picknum", "")
            intent.putExtra("type", "pod")
            intent.putExtra("doccode", "norec")
            intent.putExtra("pickup", "")
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())
        presenter.getPickupList(
            "ddlv",
            getDeviceId(), getToken(),
            "P", currentDate
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!!.extras != null) {
            try {
                // val sbUtils = SBUtils()
                val uri = Objects.requireNonNull(data.extras)
                    ?.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
                val bitmap = MediaStore.Images.Media.getBitmap(
                    contentResolver, uri
                )
                contentResolver.delete(uri!!, null, null)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                //
                //
                val filename =
                    "cpod_" + java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".jpg"
                val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
                val folder = File(extStorageDirectory, "/SmartBOL/Completed BOL/")
                if (!folder.exists()) if (!folder.mkdirs()) {
                    val i = 0
                }
                val f = File(folder, filename)
                val imgpath = f.absolutePath
                val fos = FileOutputStream(f)
                fos.write(byteArray)
                fos.close()
                val sbolnum: String = checkString(data.getStringExtra("sbolnum"))!!
                val picknum: String = checkString(data.getStringExtra("picknum"))!!
                val sbolacc: String = checkString(data.getStringExtra("sbolacc"))!!
                val shipper: String = checkString(data.getStringExtra("shipper"))!!
                val signedby: String = checkString(data.getStringExtra("signedby"))!!
                val bol: String = checkString(data.getStringExtra("bol"))!!
                val pro: String = checkString(data.getStringExtra("pro")) !!//Look into this regarding DB
                val trailer: String = checkString(data.getStringExtra("trailer"))!!
                val exception: String = checkString(data.getStringExtra("exception"))!!
                val comments: String = checkString(data.getStringExtra("comments"))!!
                val cons: String = checkString(data.getStringExtra("cons"))!!
                val conscity: String = checkString(data.getStringExtra("conscity"))!!
                val consstate: String = checkString(data.getStringExtra("consstate"))!!
                val pickup: String = checkString(data.getStringExtra("pickup"))!!
                val gps: String = ""
                // val gps: String = getLatLong(this)
                val timestamp: String = getTimestamp()!!
                val mydb = DBHelper(this)
                val bool = mydb.addShipment(
                    sbolnum, timestamp, "", "", "norec", picknum, "", bol, "",
                    shipper, "", "", "", "", "", "", "",
                    cons, "", "", conscity, consstate, "", "", "", "",
                    "", "", "", "", "y", "", "", imgpath, pro, comments, pickup
                )
                val queueObject = QueueObject(
                    "pod",
                    timestamp,
                    "",
                    imgpath,
                    "n",
                    "",
                    trailer,
                    bol,
                    comments,
                    sbolnum,
                    shipper,
                    gps,
                    "",
                    "",
                    "",
                    "",
                    "norec",
                    "",
                    picknum,
                    exception,
                    sbolacc,
                    pro,
                    cons,
                    conscity,
                    consstate,
                    pickup
                )
                mydb.addImage(queueObject)
                val file_name = imgpath.substring(imgpath.lastIndexOf("/") + 1)
                // ClsUplTrans uplTrans = new ClsUplTrans(getApplicationContext());
                // uplTrans.uploadFiles(timestamp, sbolnum, imgpath, file_name, "", trailer, bol, exception, comments, shipper, gps, "norec", picknum, sbolacc, pro, cons,conscity,consstate, pickup,signedby, DeliveryActivity.activity);
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun handlePickupListSuccess(res: PickupResponse) {
        Log.d("Neha.............", res.toString())
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())

        Log.d("Neha......1.......", res.picknum.toString())
        Log.d("Neha.....2........", res.sbolnum.toString())

//        {"txtype":"ddlv","account":"P","user":"--","pass":"<unencrypted token>","txnum":"<picknum from the delivery received>",
//            "sbolnum":"<sbolnum received>", "txdate":"<date/time>"}

       // for (x in 0..20) {
            presenter.getPickupListAck(
                "ddlv", "p",
                getDeviceId(), getToken(), res.picknum!!, res.sbolnum!!,
                currentDate
            )
       // }
      /*  presenter.getPickupListAck("ddlv","p",
            getDeviceId(), getToken(),res.picknum!!,res.sbolnum!!,
            currentDate)*/


//
    }
    override fun handlePickupListSuccessNew(res: PickupResponse) {
        Log.e("Neha...............Neha", "Neha")


//        {"txtype":"ddlv","account":"P","user":"--","pass":"<unencrypted token>","txnum":"<picknum from the delivery received>",
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


//
    }

    override fun handlePickupListFailure(message: String) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}