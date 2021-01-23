package com.example.smartboldriver.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Base64
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.models.UploadSignRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.MalformedURLException
import java.text.SimpleDateFormat
import java.util.*

class ClsUplTrans(applicationContext: Context) {

    val DOCCODE = "doccode"
    val DOCTYPE = "doctype"
    val RETCODE = "retcode"

    val FLDACC = "account"
    val FILETYPE = "filetype"
    val FLDIMGSTR = "imgstr"
    val FLDIMGSTR2 = "imgstr2"
    val FLDFILENAME = "filename"
    val FLDTRAILER = "trailer"
    val FLDSHIPNUM = "shipnum"
    val FLDBOLNUM = "bolnum"
    val FLDCOMMENT = "comments"

    val TXDATE = "txdate"
    val SBOLNUM = "sbolnum"
    val SRCLOC = "srclocation"
    val SHIPDATE = "shipdate"
    val SHIPPER = "shipper"
    val SHIPADD1 = "shipadd1"
    val SHIPADD2 = "shipadd2"
    val SHIPCITY = "shipcity"
    val SHIPSTATE = "shipstate"
    val SHIPZIP = "shipzip"
    val SHIPCNTRY = "shipcntry"
    val SHIPACC = "shipacc"
    val CONSIGNEE = "consignee"
    val CONSADD1 = "consadd1"
    val CONSADD2 = "consadd2"
    val CONSCITY = "conscity"
    val CONSSTATE = "consstate"
    val CONSZIP = "conszip"
    val CONSCNTRY = "conscntry"
    val CONSACC = "consacc"
    val CONSATTN = "consattn"
    val HAZMAT = "hazmat"
    val HMCONTACT = "hmcontact"
    val SPINSTR = "spinstr"
    val STOPNUM = "stopnum"
    val SIGNEDBY = "signedby"
    val SIGNEREMAIL = "email"
    val SIGNGPS = "signgps"
    val SIGNTIME = "signtime"
    val SIGNHASH = "signhash"
    val SIGNINFO = "signinfo"
    val SIGNNOTES = "signnotes"
    val DELVNUM = "delvnum"
    val PICKNUM = "picknum"
    val PRONUM = "pronum"

    val LONGITUDE = "longitude"
    val LATITUDE = "latitude"

    val AUTHENTICATE = "auth"
    val SENDER = "user"
    val SENDERPASS = "pass"
    val TXTYPE = "txtype"
    val TXSTATUS = "txstatus"
    val TXNUM = "txnum"
    val UPLOADDELV = "udlv"
    val UPLOADIMG = "udimg"
    val DOWNLOADDELV = "ddlv"
    val DOWNLOADPOD = "dpod"
    val UPLOADPOD = "uppod"
    val UPLOADGPS = "ugps"
    val PICKUP = "pickup"


    val PRODURL = "https://api.smartbol.com/api/txm.aspx"
    val TESTURL = "https://cltest.smartbol.com/clpod/txm.aspx"


    //var onDataLoaded : OnDataLoaded
    var userid = ""
    var userpass = ""
    var context: Context? = null
    var inputLine: String? = null
    var response:kotlin.String? = ""
    var sharedpreferences: SharedPreferences? = null

    fun ClsUplTrans(context: Context) {
        this.context = context
        sharedpreferences =
            context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    }




    fun postData(
        postStr: String,
        txType: String?,
        var1: String?,
        var2: String?
    ) {
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    /*when (txType) {
                        UPLOADDELV -> {

                            val loginRequest = PickupResponse(
                                //  txtype,account,txdate,password,user,picknum ,sbolnum,txnum1!!
                            )
                            ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
                                .getPickupList(loginRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    //mView.handleSuccessNew(it[0])
                                }, {
                                    // mView.hideProgress()
                                    // mView.handleLoginFailure(it.getErrorMessage(mContext))
                                })


                            //  if (onDataLoaded != null) onDataLoaded.dataLoaded(token)
                        }

                    }*/



                    val dbhelper = DBHelper(context)
                    val db = dbhelper.writableDatabase
                    var newValues = ContentValues()
                    var newShipmentValues =
                        ContentValues()
                    val contentValues =
                        ContentValues()
                    if (response!!.length > 0) {
                        when (txType) {
                            DOWNLOADDELV -> if (response != "[]") saveFiles(
                                response,
                                var1
                            ) //picknum field being sent to saveFiles method
                            else {
                                val sharedpreferences =
                                    context!!.getSharedPreferences(
                                        "SharedPreferences",
                                        Context.MODE_PRIVATE
                                    )
                                val editor =
                                    sharedpreferences.edit()
                                editor.putString("SAFETODOWNLOADDELIVERIES", "y")
                                editor.commit()
//                                if (onDataLoaded != null) onDataLoaded.dataLoaded("abc")
//                                if (onDataLoaded != null) onDataLoaded.pdfLoaded(var1)
                            }
                            DOWNLOADPOD -> saveSignedPdf(
                                response,
                                var1
                            ) //filepath field being sent to saveSignedPdf method is the txnum
                            UPLOADDELV -> {
                                newValues = ContentValues()
                                newShipmentValues = ContentValues()
                                val siguploaddate = SimpleDateFormat(
                                    "yyyyMMddHHmmss",
                                    Locale.getDefault()
                                ).format(Date())
                                val retval = getToken(response)
                                val sbols = retval[0]
                                val token = retval[1]
                                newValues.put("status", "y")
                                newValues.put("token", token)
                                newValues.put("uploadtimestamp", siguploaddate)
                                newShipmentValues.put("status", "yy")
                                newShipmentValues.put("token", token)
                                contentValues.put("status", "y")
                                val sbolnums =
                                    sbols!!.split(",".toRegex()).toTypedArray()
                                for (sbol in sbolnums) {
                                    db.update(
                                        "images",
                                        newValues,
                                        "type = 'sig' and sbolnum = ?",
                                        arrayOf(sbol)
                                    )
                                    db.update(
                                        "images",
                                        contentValues,
                                        "type = 'pdf' and token = '' and sbolnum = ?",
                                        arrayOf(sbol)
                                    )
                                    db.update(
                                        "shipments",
                                        newShipmentValues,
                                        "sbolnum = ?",
                                        arrayOf(sbol)
                                    )
                                }
                              //  if (onDataLoaded != null) onDataLoaded.dataLoaded(token)
                            }
                            UPLOADIMG -> {
                                val jsonArray = JSONArray(response)
                                val jsonObject = jsonArray.getJSONObject(0)
                                if (jsonObject.getString(RETCODE) == "0") {
                                    val uploaddate = SimpleDateFormat(
                                        "yyyyMMddHHmmss",
                                        Locale.getDefault()
                                    ).format(Date())
                                    contentValues.put("uploadtimestamp", uploaddate)
                                    contentValues.put("status", "y")
                                    db.update(
                                        "images",
                                        contentValues,
                                        "path = ? ",
                                        arrayOf(var1)
                                    )
                                }
                            }
                            UPLOADPOD -> {
                                val jsonArray1 = JSONArray(response)
                                val jsonObject1 = jsonArray1.getJSONObject(0)
                                val picknum =
                                    jsonObject1.getString(PICKNUM)
                                val sbolnum =
                                    jsonObject1.getString(SBOLNUM)
                                if (jsonObject1.getString(RETCODE) == "0") {
                                    val uploaddate = SimpleDateFormat(
                                        "yyyyMMddHHmmss",
                                        Locale.getDefault()
                                    ).format(Date())
                                    if (var2 == "rec") {
                                        newShipmentValues.put("path", var1)
                                        newShipmentValues.put("downloaddate", uploaddate)
                                        newShipmentValues.put("status", "yyy")
                                        val i = db.update(
                                            "shipments",
                                            newShipmentValues,
                                            "sbolnum = ? ",
                                            arrayOf(sbolnum)
                                        )
                                    } else if (var2 == "norec") {
                                        newShipmentValues.put("sbolnum", sbolnum)
                                        newShipmentValues.put("picknum", picknum)
                                        newShipmentValues.put("downloaddate", uploaddate)
                                        newShipmentValues.put("status", "yyy")
                                        db.update(
                                            "shipments",
                                            newShipmentValues,
                                            "path = ? ",
                                            arrayOf(var1)
                                        )
                                    }
                                    contentValues.put("sbolnum", sbolnum)
                                    contentValues.put("picknum", picknum)
                                    contentValues.put("uploadtimestamp", uploaddate)
                                    contentValues.put("status", "y")
                                    db.update(
                                        "images",
                                        contentValues,
                                        "path = ? ",
                                        arrayOf(var1)
                                    )
//                                    if (onDataLoaded != null) onDataLoaded.dataLoaded("abc")
//                                    if (onDataLoaded != null) onDataLoaded.pdfLoaded(var1)
                                }
                            }
                            UPLOADGPS -> {
                                val jArray = JSONArray(response)
                                val jObject = jArray.getJSONObject(0)
                                if (jObject.getString(RETCODE) == "0") {
//                                    String latitude = var1;
//                                    String longitude = var2;
//                                    if(!jObject.getString(LATITUDE).equals("")) {
//                                        latitude = jObject.getString(LATITUDE);
//                                    }
//                                    else{break;}
//                                    if(!jObject.getString(LONGITUDE).equals("")) {
//                                        longitude = jObject.getString(LONGITUDE);
//                                    }
//                                    else{break;}
                                    //val sbUtils = SBUtils()
                                    val returntime: String = getTimestamp()!!
                                    contentValues.put("status", "y")
                                    contentValues.put("returntime", returntime)
                                    db.update(
                                        "gps",
                                        contentValues,
                                        "latitude = ? and longitude = ?",
                                        arrayOf(var1, var2)
                                    )
                                }
                            }
                        }
                    }
                } catch (mex: MalformedURLException) {
                    val err = mex.message
                } catch (iox: IOException) {
                    val err = iox.message
                } catch (e: Exception) {
                    e.printStackTrace()
                    //createDialog("Error", "Cannot Estabilish Connection");
                }
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


   /* fun uploadGPS(
        timestamp: String?,
        latitude: String?,
        longitude: String?,
        trailer: String?
    ) {
        try {
            val jsonArray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                json.put(TXTYPE, UPLOADGPS)
                json.put(TXDATE, timestamp)
                json.put(LONGITUDE, longitude)
                json.put(LATITUDE, latitude)
                json.put(FLDTRAILER, "")

                // add to array
                jsonArray.put(json)
                postData(jsonArray.toString(), UPLOADGPS, latitude, longitude)
            }
        } catch (e: Exception) {
            val err = e.message
        }
    }
*/

    fun getToken(jsonStr: String?): Array<String?> {
        var token = ""
        var sbolnums = ""
        val retval = arrayOfNulls<String>(2)
        try {
            val jsonArray = JSONArray(jsonStr)
            val jsonObject = jsonArray.getJSONObject(0)
            token = jsonObject.getString(TXNUM)
            sbolnums = jsonObject.getString(SBOLNUM)
        } catch (e: Exception) {
            val x = e.message
        }
        retval[0] = sbolnums
        retval[1] = token
        return retval
    }

   fun getSignedPdf(token: String?, activity: Activity?) {
       // if (activity != null) onDataLoaded = activity as OnDataLoaded?
        try {
            val jsonarray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                json.put(TXNUM, token)
                json.put(TXTYPE,DOWNLOADPOD)
                jsonarray.put(json)
                postData(jsonarray.toString(), DOWNLOADPOD, token, "")
            }
        } catch (e: Exception) {
            val s = e.toString()
        }
    }

    fun getDeliveries(picknum: String?, activity: Activity?) {
       // DeliveryObject.deliveryList.clear()
      //  if (activity != null) onDataLoaded = activity as OnDataLoaded?
        if (picknum == null || picknum == "") {
            try {
                val jsonArray = JSONArray()
                val json = JSONObject()
                if (addAccountInfo(json)) {
                    json.put(TXTYPE, DOWNLOADDELV)
                    jsonArray.put(json)
                    postData(jsonArray.toString(), DOWNLOADDELV, "", "D")
                }
            } catch (e: Exception) {
                val x = e.message
            }
        } else {
            try {
                val jsonArray = JSONArray()
                val json = JSONObject()
                if (addAccountInfo(json)) {
                    json.put(TXTYPE, DOWNLOADDELV)
                    json.put(PICKNUM, picknum)
                    jsonArray.put(json)
                    postData(jsonArray.toString(), "ddlv", picknum, "D")
                }
            } catch (e: Exception) {
                val x = e.message
            }
        }
    }


    fun getOtherDeliveries(picknum: String?, sbol: String?) {
        try {
            val jsonArray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                json.put(TXTYPE, DOWNLOADDELV)
                json.put(PICKNUM, picknum)
                json.put(SBOLNUM, sbol)
                jsonArray.put(json)
                postData(jsonArray.toString(), "ddlv", picknum, "D")
            }
        } catch (e: Exception) {
            val x = e.message
        }
    }

/*    fun getPickups(picknum: String?, activity: Activity?) {
        PickupObject.pickupList.clear()
        if (activity != null) onDataLoaded = activity as OnDataLoaded?
        if (picknum == null || picknum == "") {
            try {
                val jsonArray = JSONArray()
                val json = JSONObject()
                if (addAccountInfo(json)) {
                    json.put(TXTYPE, DOWNLOADDELV)
                    jsonArray.put(json)
                    postData(jsonArray.toString(), DOWNLOADDELV, "", "P")
                }
            } catch (e: Exception) {
                val x = e.message
            }
        } else {
            try {
                val jsonArray = JSONArray()
                val json = JSONObject()
                if (addAccountInfo(json)) {
                    json.put(TXTYPE, DOWNLOADDELV)
                    json.put(PICKNUM, picknum)
                    jsonArray.put(json)
                    postData(jsonArray.toString(), "ddlv", picknum, "P")
                }
            } catch (e: Exception) {
                val x = e.message
            }
        }
    }*/


    fun getOtherPickups(picknum: String?, sbol: String?) {
        try {
            val jsonArray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                json.put(TXTYPE, DOWNLOADDELV)
                json.put(PICKNUM, picknum)
                json.put(SBOLNUM, sbol)
                jsonArray.put(json)
                postData(jsonArray.toString(), "ddlv", picknum, "P")
            }
        } catch (e: Exception) {
            val x = e.message
        }
    }

    fun saveFiles(jsonStr: String?, pickupnum: String?) {
        try {
            val filename =
                java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
            val jsonArray = JSONArray(jsonStr)
            val jsonObject = jsonArray.getJSONObject(0)
            var filestr = ""
            try {
                filestr = jsonObject.getString(FLDIMGSTR)
            } catch (e: Exception) {
            }
            var pdfpath = ""
            if (filestr.length > 0) {
                val extStorageDirectory =
                    Environment.getExternalStorageDirectory().toString()
                val folder =
                    File(extStorageDirectory, "/SmartBOL/Pending BOL/")
                if (!folder.exists()) if (!folder.mkdirs()) {
                    val i = 0
                }
                val f = File(folder, filename)
                pdfpath = f.absolutePath
                val byts = getBytsFromBase64String(filestr)
                val fos = FileOutputStream(f)
                fos.write(byts)
                fos.close()
            }
            val sbolnum = jsonObject.getString(SBOLNUM)
            val txdate = jsonObject.getString(TXDATE)
            val shipdate = jsonObject.getString(SHIPDATE)
            val srclocation = jsonObject.getString(SRCLOC)
            val doccode = jsonObject.getString(DOCCODE)
            val picknum = jsonObject.getString(PICKNUM)
            val delvnum = jsonObject.getString(DELVNUM)
            val bolnum = jsonObject.getString(FLDBOLNUM)
            val shipnum = jsonObject.getString(FLDSHIPNUM)
            val shipper = jsonObject.getString(SHIPPER)
            val shipadd1 = jsonObject.getString(SHIPADD1)
            val shipadd2 = jsonObject.getString(SHIPADD2)
            val shipcity = jsonObject.getString(SHIPCITY)
            val shipstate = jsonObject.getString(SHIPSTATE)
            val shipzip = jsonObject.getString(SHIPZIP)
            val shipcntry = jsonObject.getString(SHIPCNTRY)
            val shipacc = jsonObject.getString(SHIPACC)
            val consignee = jsonObject.getString(CONSIGNEE)
            val consadd1 = jsonObject.getString(CONSADD1)
            val consadd2 = jsonObject.getString(CONSADD2)
            val conscity = jsonObject.getString(CONSCITY)
            val consstate = jsonObject.getString(CONSSTATE)
            val conszip = jsonObject.getString(CONSZIP)
            val conscntry = jsonObject.getString(CONSCNTRY)
            val consattn = jsonObject.getString(CONSATTN)
            val consacc = jsonObject.getString(CONSACC)
            val hazmat = jsonObject.getString(HAZMAT)
            val hmcontact = jsonObject.getString(HMCONTACT)
            val spinstr = jsonObject.getString(SPINSTR)
            val stopnum = jsonObject.getString(STOPNUM)
            val pronum = jsonObject.getString(PRONUM)
            val pickup = jsonObject.getString(PICKUP)
            val comments = ""
            val downloaddate =
                SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    .format(Date())
            val mydb = DBHelper(context)
            mydb.addUnsignedBOL("pdf", sbolnum, bolnum, downloaddate, pdfpath, picknum, "n")
            if (mydb.countSBOLNum(sbolnum) === 0) {
                mydb.addShipment(
                    sbolnum,
                    txdate,
                    shipdate,
                    srclocation,
                    doccode,
                    picknum,
                    delvnum,
                    bolnum,
                    shipnum,
                    shipper,
                    shipadd1,
                    shipadd2,
                    shipcity,
                    shipstate,
                    shipzip,
                    shipcntry,
                    shipacc,
                    consignee,
                    consadd1,
                    consadd2,
                    conscity,
                    consstate,
                    conszip,
                    conscntry,
                    consattn,
                    consacc,
                    hazmat,
                    hmcontact,
                    spinstr,
                    stopnum,
                    "n",
                    downloaddate,
                    "",
                    pdfpath,
                    pronum,
                    comments,
                    pickup
                )
            } else {
                mydb.updateShipment(
                    sbolnum,
                    txdate,
                    shipdate,
                    srclocation,
                    doccode,
                    picknum,
                    delvnum,
                    bolnum,
                    shipnum,
                    shipper,
                    shipadd1,
                    shipadd2,
                    shipcity,
                    shipstate,
                    shipzip,
                    shipcntry,
                    shipacc,
                    consignee,
                    consadd1,
                    consadd2,
                    conscity,
                    consstate,
                    conszip,
                    conscntry,
                    consattn,
                    consacc,
                    hazmat,
                    hmcontact,
                    spinstr,
                    stopnum,
                    "n",
                    downloaddate,
                    "",
                    pdfpath,
                    pronum,
                    comments,
                    pickup
                )
            }
            if (pickup == "D") {
                getOtherDeliveries(picknum, sbolnum)
            } else if (pickup == "P") {
                getOtherPickups(picknum, sbolnum)
            }
        } catch (e: Exception) {
            val err = e.message
        }
    }

    fun uploadFiles(
        timestamp: String?,
        sbolnum: String?,
        filePath: String?,
        fileName: String?,
        shipmentNum: String?,
        trailerNum: String?,
        bolNum: String?,
        exception: String?,
        comments: String?,
        shipper: String?,
        gps: String?,
        doccode: String,
        picknum: String?,
        account: String?,
        pro: String?,
        cons: String?,
        conscity: String?,
        consstate: String?,
        pickup: String,
        signedby: String?,
        activity: Activity?
    ) {
       // if (activity != null) onDataLoaded = activity as OnDataLoaded?
      //  DeliveryObject.deliveryList.clear()
        //PickupObject.pickupList.clear()
        var latitude = ""
        var longitude = ""
        if (gps != null && gps.length > 1) {
            val latlong = gps.split(" ".toRegex()).toTypedArray()
            latitude = latlong[0]
            longitude = latlong[1]
        }
        val file = File(filePath)
        try {
            val jsonArray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                if (doccode == "pic") {
                    json.put(TXTYPE, UPLOADIMG)
                    if (pickup == "P") {
                        json.put("docname", "Pickup Photo")
                    } else if (pickup == "D") {
                        json.put("docname", "Delivery Photo")
                    }
                } else if (doccode == "rec" || doccode == "norec") {
                    json.put(TXTYPE, UPLOADPOD)
                    if (pickup == "P") {
                        json.put("docname", "BOL - Pickup(Image)")
                    } else if (pickup == "D") {
                        json.put("docname", "POD - Receipt(Image)")
                    }
                }
                //   File file = new File(context.getExternalFilesDir(folderName), filename);
                var imgString = ""
                //  if (file.exists()) {
                imgString = getBase64ImgString(file)
                //  }
                if (imgString.length > 0) {
                    val dateandtime =
                        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                            .format(Date())
                    json.put(FILETYPE, "jpg")
                    json.put(FLDFILENAME, fileName)
                    json.put(DOCCODE, doccode)
                    json.put(TXDATE, timestamp)
                    json.put(FLDACC, account)
                    json.put(SBOLNUM, sbolnum)
                    json.put(PRONUM, pro)
                    json.put(PICKNUM, picknum)
                    json.put(FLDBOLNUM, bolNum)
                    json.put(FLDSHIPNUM, shipmentNum)
                    json.put(FLDTRAILER, trailerNum)
                    json.put(SIGNEDBY, signedby)
                    json.put(SHIPPER, shipper)
                    json.put(CONSIGNEE, cons)
                    json.put(CONSCITY, conscity)
                    json.put(CONSSTATE, consstate)
                    json.put(TXSTATUS, exception)
                    json.put(FLDCOMMENT, comments)
                    json.put(TXDATE, dateandtime)
                    json.put(SIGNGPS, "")
                    json.put(LATITUDE, latitude)
                    json.put(LONGITUDE, longitude)
                    json.put(PICKUP, pickup)
                    json.put(FLDIMGSTR, imgString)
                    // add to array
                    jsonArray.put(json)
                    if (doccode == "norec") {
                        postData(jsonArray.toString(), UPLOADPOD, filePath, doccode)
                    } else if (doccode == "rec") {
                        postData(jsonArray.toString(), UPLOADPOD, filePath, doccode)
                    } else if (doccode == "pic") {
                        postData(jsonArray.toString(), UPLOADIMG, filePath, "")
                    }
                }
            }
        } catch (e: Exception) {
            val err = e.message
        }
    }


    @Throws(JSONException::class)
    fun addAccountInfo(json: JSONObject): Boolean {
        try {
           /* userid = Login.Name
            userpass = Login.Pass*/


//            SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
//
//            if (sharedpreferences.contains("NAME")) {
//                userid = (sharedpreferences.getString("NAME", ""));
//            }
//            if (sharedpreferences.contains("PASS")) {
//                userpass = (sharedpreferences.getString("PASS", ""));
//            }
        } catch (e: java.lang.Exception) {
            val x = e.message
        }
        if (userid == "") {
            return false
        }
        json.put(SENDER, userid)
        json.put(SENDERPASS, userpass)
        return true
    }

    fun uploadSignature(
        signedby: String?,
        imgString: String?,
        signhash: String?,
        signtime: String?,
        signgps: String,
        signnote: String?,
        sbolnum: String?,
        email: String?,
        picknum: String?,
        exception: String?,
        pickup: String,
        activity: Activity?
    ) {
       // if (activity != null) onDataLoaded = activity as OnDataLoaded?
        try {
            var latitude = ""
            var longitude = ""
            if (signgps.length > 1) {
                val latlong =
                    signgps.split(" ".toRegex()).toTypedArray()
                latitude = latlong[0]
                longitude = latlong[1]
            }
            val jsonArray = JSONArray()
            val json = JSONObject()
            if (addAccountInfo(json)) {
                json.put(TXTYPE, UPLOADDELV)
                json.put(DOCCODE, "S")
                json.put(SBOLNUM, sbolnum)
                json.put(PICKNUM, picknum)
                json.put(PICKUP, pickup)
                json.put(TXDATE, signtime)
                json.put(SIGNTIME, signtime)
                json.put(SIGNGPS, "")
                json.put(LATITUDE, latitude)
                json.put(LONGITUDE, longitude)
                json.put(SIGNEDBY, signedby)
                json.put(SIGNEREMAIL, email)
                json.put(FLDCOMMENT, signnote)
                json.put(FLDIMGSTR, imgString)
                json.put(SIGNHASH, signhash)
                json.put(TXSTATUS, exception)
                //              json.put(TXNUM, token);
                if (pickup == "P") {
                    json.put("docname", "BOL - Pickup")
                } else if (pickup == "D") {
                    json.put("docname", "POD - Receipt")
                }
                jsonArray.put(json)
                postData(jsonArray.toString(), UPLOADDELV, null, sbolnum)
            }
        } catch (e: Exception) {
            val err = e.message
        }
    }

    fun saveSignedPdf(jsonStr: String?, token: String?) {
        try {
            if (response != "[]") {
                val jsonArray = JSONArray(jsonStr)
                val jsonObject = jsonArray.getJSONObject(0)
                //JSONObject jsonObject = new JSONObject(jsonStr);
                val txnum2 = jsonObject.getString("txnum2")
                //String pickup = jsonObject.getString("pickup");
                val shipnum = jsonObject.getString(FLDSHIPNUM)
                val bolnum = jsonObject.getString(FLDBOLNUM)
                val filename = token + "_" + bolnum + "_" + shipnum + ".pdf"
                val extStorageDirectory =
                    Environment.getExternalStorageDirectory().toString()
                val folder =
                    File(extStorageDirectory, "/SmartBOL/Completed BOL/")
                if (!folder.exists()) if (!folder.mkdirs()) {
                    val i = 0
                }
                val f = File(folder, filename)
                val newpdfpath = f.absolutePath
                val filestr = jsonObject.getString(FLDIMGSTR)
                val byts = getBytsFromBase64String(filestr)
                val fos = FileOutputStream(f)
                fos.write(byts)
                fos.close()
                val newdownloaddate =
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        .format(Date())
                val dbhelper = DBHelper(context)
                val db = dbhelper.writableDatabase
                val sbolnum = dbhelper.getSbolNum(bolnum, shipnum)
                val picknum = dbhelper.getPickNum(sbolnum!!)
                val newValues = ContentValues()
                newValues.put("type", "pdf")
                newValues.put("path", newpdfpath)
                newValues.put("timestamp", newdownloaddate)
                newValues.put("sbolnum", sbolnum)
                newValues.put("picknum", picknum)
                // newValues.put("pickup", pickup);
                newValues.put("token", token)
                val bool = db.insert("images", null, newValues)
                val newShipmentValues =
                    ContentValues()
                newShipmentValues.put("status", "yyy")
                newShipmentValues.put("path", newpdfpath)
                db.update(
                    "shipments",
                    newShipmentValues,
                    "sbolnum = ? AND token = ?",
                    arrayOf(sbolnum, token)
                )
                val jsonarray = JSONArray()
                val json = JSONObject()
                if (addAccountInfo(json)) {
                    json.put(TXNUM, token)
                    json.put("txnum2", txnum2)
                    json.put(TXTYPE, DOWNLOADPOD)
                    jsonarray.put(json)
                    postData(jsonarray.toString(), DOWNLOADPOD, token, sbolnum)
                }
            }
            //else if (onDataLoaded != null) onDataLoaded.pdfLoaded(token)
        } catch (e: Exception) {
            val x = e.message
        }
    }


    fun getBase64ImgString(file: File): String { // String filename) {
        var retString = ""
        if (file.exists()) {
            try {
                val inputStream: InputStream = FileInputStream(file)
                var bytes: ByteArray
                val buffer = ByteArray(8192)
                var bytesRead: Int
                val output = ByteArrayOutputStream()
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }
                retString = Base64.encodeToString(
                    output.toByteArray(),
                    Base64.DEFAULT
                )
            } catch (e: Exception) {
                val x = e.message
                e.printStackTrace()
            }
        }
        return retString
    }

    fun getBytsFromBase64String(b64str: String?): ByteArray {
        return Base64.decode(b64str, Base64.DEFAULT)
    }

}