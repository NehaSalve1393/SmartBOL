package com.example.smartboldriver.View

import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.features.shipments.DeliveryListActivity
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.UploadDataRequest
import com.example.smartboldriver.utils.*
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PODBlankPageActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_p_o_d_blank_page)
//    }

    private var sbolnum: String? = null
    private var bolnum: String? = null
    private var picknum: String? = null
    private var type: String? = null
    private var doccode: String? = null
    private var pickup: String? = null
    private var receiverName: String? = null
    private var commentAdded: String? = null
    private var ExceptionScring: String? = null
    var firstload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = this.intent
        this.sbolnum = i.getStringExtra("sbol")
        this.bolnum = i.getStringExtra("bol")
        this.picknum = i.getStringExtra("picknum")
        this.type = i.getStringExtra("type")
        this.doccode = i.getStringExtra("doccode")
        this.pickup = i.getStringExtra("pickup")
        this.firstload = true
        val REQUEST_CODE = 99
        val preference = ScanConstants.OPEN_CAMERA
        val intent =
            Intent(this, ScanActivity::class.java)
        intent.putExtra("sbol", this.sbolnum)
        intent.putExtra("picknum", this.picknum)
        intent.putExtra("type", this.type)
        intent.putExtra("doccode", this.doccode)
        intent.putExtra("pickup", this.pickup)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        this.startActivityForResult(intent, REQUEST_CODE)
       // this.finish()
    }

    override fun onResume() {
        super.onResume()
//        if (!this.firstload) {
//            val cn = ComponentName(
//                this,
//                DeliveryListActivity::class.java
//            )
//            val i = Intent().setComponent(cn)
//            i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            this.startActivity(i)
//        }
        this.firstload = false
    }

    private fun showDialog1(title: String,cons:String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val receiver_name = dialog.findViewById(R.id.et_recivername) as EditText
        val et_comments = dialog.findViewById(R.id.et_comments) as EditText

        val exceptionSpinner =
            dialog.findViewById<Spinner>(R.id.exceptionSpinner1)
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

        val yesBtn = dialog.findViewById(R.id.btn_upload) as Button

        yesBtn.setOnClickListener {
            var exception = ""
            if (exceptionSpinner!!.selectedItem.toString() == "Short") exception =
                "S" else if (exceptionSpinner!!.selectedItem.toString() == "Damaged") exception =
                "D" else if (exceptionSpinner!!.selectedItem.toString() == "Refused") exception =
                "R" else if (exceptionSpinner!!.selectedItem.toString() == "Other") exception = "O"
            saveAndUploadData(title,receiver_name.text.toString(),et_comments.text.toString(),exception,cons)
            dialog.dismiss()
        }
      //  noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
    fun saveAndUploadData(
        imgpath: String,
        name: String,
        comments: String,
        exception: String,
        cons: String
    ) {
        val signedby: String = checkString(name)!!
        val exception: String = checkString(exception)!!
        val comments: String = checkString(comments)!!

        //String pickup = sbUtils.checkString(data.getStringExtra("pickup"));
        val gps: String = ""
        val timestamp: String = getTimestamp()!!
        val mydb = DBHelper(this)
        val db: SQLiteDatabase = mydb.getWritableDatabase()
        val queueObject = QueueObject(
            this.type!!, timestamp, "", imgpath, "n", "",
            "", this.bolnum!!, comments, this.sbolnum!!, signedby, gps,
            "", signedby, "",
            "", this.doccode!!, "", this.picknum!!, exception,
            "", "", cons,
            "",
            "",
            this.pickup!!
        )
        mydb.addImage(queueObject)
        if (this.type == "pod") {
            val newShipmentValues =
                ContentValues()
            newShipmentValues.put("status", "y")
            db.update(
                "shipments",
                newShipmentValues,
                "sbolnum = ?",
                arrayOf(this.sbolnum)
            )

            val request = UploadDataRequest(
                getDeviceId(),
                getToken(),
                "uppod",
                this.doccode,
                "POD - Receipt(Image)",
                "jpg",
                "filename",
                this.picknum!!,
                this.sbolnum,
                "",
                "",
                this.bolnum,
                "",
                comments,
                "",
                "",
                "",
                "",
                "",
                "",
                name,
                "",
                "",
                this.pickup,
                imgpath,
                "P",
                timestamp
            )
            uploadFile(request, imgpath, this.doccode!!, applicationContext)
        }
        val cn = ComponentName(
                this,
                DeliveryListActivity::class.java
            )
            val i = Intent().setComponent(cn)
            i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(i)

    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
                // try {
                // val sbUtils = SBUtils()
                //showDialog("")
                val uri =
                    data!!.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                this.contentResolver.delete(uri!!, null, null)
                val byteArrayOutputStream =
                    ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                )
                val byteArray = byteArrayOutputStream.toByteArray()
                var filename =
                    "cpod_" + java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".jpg"
                val extStorageDirectory =
                    Environment.getExternalStorageDirectory().toString()
                var folder =
                    File(extStorageDirectory, "/SmartBOL/Completed BOL/")
                if (this.type == "img") {
                    folder = File(extStorageDirectory, "/SmartBOL/Images/")
                    filename =
                        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                            .format(Date()) + ".jpg"
                }
                if (!folder.exists()) if (!folder.mkdirs()) {
                    val i = 0
                }
                val f = File(folder, filename)
                val imgpath = f.absolutePath
                val fos = FileOutputStream(f)
                fos.write(byteArray)
                fos.close()
                val cons: String = checkString(data.getStringExtra("cons"))!!
                showDialog1(imgpath,cons)


                //  }
                //  this.finish()
//            catch (e: IOException) {
//                e.printStackTrace()
//            }

            }

        }
}