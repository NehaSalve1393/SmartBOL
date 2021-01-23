package com.example.smartboldriver.features.shipments

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.View.PdfViewerActivity
import com.example.smartboldriver.features.onDataLoaded
import com.example.smartboldriver.models.UploadSignRequest
import com.example.smartboldriver.models.pickup.PickupResponse
import com.example.smartboldriver.utils.*
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_del_sign.*

class DelSignActivity :  AppCompatActivity(), onDataLoaded {

    var exceptionSpinner: Spinner? = null
    var etSigner: EditText? = null
    var signedby: String? = null
    var etEmail: EditText? = null
    var etNotes: EditText? = null


    var btnback: Button? = null
    var btnSubmit: Button? = null
    var signView: SignaturePad? = null
    var signature: Bitmap? = null
    var pickup: String? = null
    var sbolnums: ArrayList<String>? = null
    var picknums: ArrayList<String>? = null
    var context: Context? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        try {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
        return true
    }

    override fun dataLoaded(token: String?) {
        /* this.runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        val sharedpreferences =
            getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        var downloadBOL: String? = ""
        if (sharedpreferences.contains("Pod")) {
            downloadBOL = sharedpreferences.getString("Pod", "y")
        }
        if (downloadBOL == "y") {
            val trans = ClsUplTrans(applicationContext)
            trans.getSignedPdf(token, this)
        } else {
            val intent = Intent(
                applicationContext,
                DeliveryListActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        /*}
        });*/
    }

    override fun pdfLoaded(token: String?) {
        runOnUiThread {
            val mydb = DBHelper(applicationContext)
            val data: HashMap<*, *>? = mydb.getSignedPDF(token!!)
            val picks =
                data!!["picks"] as ArrayList<String>?
            val sbols =
                data["sbols"] as ArrayList<String>?
            val bols =
                data["bols"] as ArrayList<String>?
            val pdfs =
                data["pdfs"] as ArrayList<String>?
            val intent = Intent(
                applicationContext,
                PdfViewerActivity::class.java
            )
            intent.putStringArrayListExtra("multipick", picks)
            intent.putStringArrayListExtra("multisbol", sbols)
            intent.putStringArrayListExtra("multibol", bols)
            intent.putStringArrayListExtra("multipdfs", pdfs)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_sign)
        sbolnums = intent.getStringArrayListExtra("sbolnums")
        picknums = intent.getStringArrayListExtra("picknums")
        pickup = intent.getStringExtra("pickup")
        btnback = findViewById(R.id.btnBack) as Button
        btnSubmit = findViewById(R.id.btnSubmit) as Button
        signView = findViewById(R.id.signView) as SignaturePad
       // signView.setStrokeWidth(this)
    //    val sw: Float = signView.STROKE_WIDTH
        etSigner = findViewById(R.id.etSigner) as EditText
        etEmail = findViewById(R.id.etEmail) as EditText
        etNotes = findViewById(R.id.etNotes) as EditText
//
        exceptionSpinner =
            findViewById<Spinner>(R.id.exceptionSpinner)
        val list: MutableList<String> =
            ArrayList()
        list.add("-- Select an option --")
        list.add("No issue with pickup/delivery")
        list.add("Short")
        list.add("Damaged")
        list.add("Refused")
        list.add("Other")
        val dataAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, list
            )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exceptionSpinner!!.adapter = dataAdapter
    }


    fun onCancelClick(view: View?) {
        signView!!.clear()
        finish()
    }

    fun onClearClick(v: View?) {
        signView!!.clear()
    }

    fun onSubmitClick(view: View?) {
        try {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
        val signedby = etSigner!!.text.toString()
        val email = etEmail!!.text.toString()
        val comments = etNotes!!.text.toString()
        if (signView!!.getSignatureBitmap()!=null) {
            if (signedby.length > 0) {
                if (email.length > 0) {
                    val regex =
                        "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$"
                    val pattern = Pattern.compile(regex)
                    val matcher = pattern.matcher(email)
                    if (matcher.matches()) {
                        if (exceptionSpinner!!.selectedItem
                                .toString() != "-- Select an option --" || exceptionSpinner!!.visibility == View.GONE
                        ) {
                            if (exceptionSpinner!!.selectedItem
                                    .toString() != "No issue with pickup/delivery"
                            ) {
                                if (comments.length > 0) {
                                    processSignature(signedby, email, comments)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Please describe issue in comments box",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                processSignature(signedby, email, comments)
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Please confirm if issue with delivery",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Email not valid ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (exceptionSpinner!!.selectedItem
                            .toString() != "-- Select an option --" || exceptionSpinner!!.visibility == View.GONE
                    ) {
                        if (exceptionSpinner!!.selectedItem
                                .toString() != "No issue with pickup/delivery"
                        ) {
                            if (comments.length > 0) {
                                processSignature(signedby, email, comments)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Please describe issue in comments box",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            processSignature(signedby, email, comments)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Please confirm if issue with delivery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Please enter signer's name",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Please sign for delivery",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun processSignature(
        signedby: String?,
        email: String?,
        comment: String?
    ) {
       // val points: Array<Point> = SignatureView.getPoints()
      //  val signinfo = points.toString()
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
        if (exceptionSpinner!!.selectedItem.toString() == "Short") exception =
            "S" else if (exceptionSpinner!!.selectedItem.toString() == "Damaged") exception =
            "D" else if (exceptionSpinner!!.selectedItem.toString() == "Refused") exception =
            "R" else if (exceptionSpinner!!.selectedItem.toString() == "Other") exception = "O"
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

            for (SBOL in sbolnums!!) {
                sbolnumbers = "$sbolnumbers,$SBOL"
                val picknum = picknums!![sbolnums!!.indexOf(SBOL)]
                picknumbers = "$picknumbers,$picknum"
                signtime =
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        .format(Date())
                signhash = java.lang.Long.toString(hash)
                val newContentValues =
                    ContentValues()
                newContentValues.put("type", "sig")
                newContentValues.put("timestamp", signtime)
                newContentValues.put("sbolnum", SBOL)
                newContentValues.put("signedby", signedby)
                newContentValues.put("comment", comment)
                newContentValues.put("gps", gps)
                newContentValues.put("signimage", imgString)
                newContentValues.put("signinfo", signinfo)
                newContentValues.put("signhash", signhash)
                newContentValues.put("status", "n")
                newContentValues.put("doccode", "s")
                newContentValues.put("email", email)
                newContentValues.put("picknum", picknum)
                newContentValues.put("pickup", pickup)
                newContentValues.put("exception", exception)
                db.insert("img", null, newContentValues)
                val newShipmentValues =
                    ContentValues()
                newShipmentValues.put("status", "y")
                db.update(
                    "shipments",
                    newShipmentValues,
                    "sbolnum = ?",
                    arrayOf(SBOL)
                )
            }
            sbolnumbers = sbolnumbers.substring(1)
            picknumbers = picknumbers.substring(1)
            val trans = ClsUplTrans(applicationContext)

//check network condition
            val req  = UploadSignRequest(
                getDeviceId(), getToken(),"P","udlv","S","POD - Receipt",picknumbers,pickup!!,sbolnumbers,signedby,
                gps,signhash,signtime,"",imgString,"","",
                getTimestamp(),exception,comment,email)
            UploadDelSign(
                req,this
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        val output = Intent()
        setResult(Activity.RESULT_OK, output)
        signView!!.clear()
        finish()
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