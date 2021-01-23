package com.example.smartboldriver.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.smartboldriver.SmartBOLDriver
import com.example.smartboldriver.api.SmartBOL24Retrofit
import com.example.smartboldriver.api.ThisRetrofit
import com.example.smartboldriver.api.services.MasterAPIService
import com.example.smartboldriver.features.authentication.LoginActivity
import com.example.smartboldriver.features.onDataLoaded
import com.example.smartboldriver.models.SaveSignPdfRequest
import com.example.smartboldriver.models.UploadDataRequest
import com.example.smartboldriver.models.UploadSignRequest
import com.example.smartboldriver.models.User
import com.example.smartboldriver.models.pickup.PickupResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.*


//var onDataLoaded: OnData? = null
var userid = ""
var userpass = ""
var context: Context? = null
var inputLine: String? = null
var response:kotlin.String? = ""
var sharedpreferences: SharedPreferences? = null
var OnDataLoaded : onDataLoaded? =null

fun getDeviceId(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(DEVICE_ID)?:""

fun getToken(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(TOKEN)
    ?: ""

fun getUserId(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(USER_ID)
    ?: ""

fun getPassword(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(PASSWORD)
    ?: ""

fun getEncryption(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(ENCRYPTION)
    ?: ""

fun getCompanyAccountCode(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(
    COMP_ACCOUNT_CODE
)
    ?: ""

fun getDriverName(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(
    Driver_Name
)
    ?: ""

fun getAccount(): String = SmartBOLPref(SmartBOLDriver.applicationContext()).getString(ACCOUNT)
    ?: ""


fun saveUser(user: User) {
    Log.d("check response", user.toString())
    with(SmartBOLPref(SmartBOLDriver.applicationContext())) {
        saveString(DEVICE_ID, user.deviceId.toString())
        saveString(USER_ID, user.userId.toString())
        saveString(TOKEN, user.token.toString())
        saveString(ENCRYPTION, user.encryption.toString())
        saveString(COMP_ACCOUNT_CODE, user.companyCode.toString())
        saveString(ACCOUNT, user.account.toString())

    }
}
fun saveDriverDetails(name: String){
    with(SmartBOLPref(SmartBOLDriver.applicationContext())) {

        saveString(Driver_Name, name)

    }

}
fun localLogout(context: Context) {

    with(SmartBOLPref(SmartBOLDriver.applicationContext())) {
        clear()
        //saveString(PREF_FCM_TOKEN, fmcToken)
    }

    try {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        (context as Activity).finishAffinity()
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun getFormattedDate(dateAsString: String): Date? {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(dateAsString)
    } catch (e: java.lang.Exception) {

        null
    }
}



interface ToolbarListener {
    fun setUpTitle(title: String)
}

fun checkString(string: String?): String? {
    return string ?: ""
}
fun getTimestamp(): String? {
    return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        .format(Date())
}

@Throws(IOException::class)
 fun getExifOrientation(src: String): Int {
    var orientation = 1
    try {
        /**
         * if your are targeting only api level >= 5
         * ExifInterface exif = new ExifInterface(src);
         * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
         */
        if (Build.VERSION.SDK_INT >= 5) {
            val exifClass =
                Class.forName("android.media.ExifInterface")
            val exifConstructor = exifClass.getConstructor(
                *arrayOf<Class<*>>(
                    String::class.java
                )
            )
            val exifInstance = exifConstructor.newInstance(*arrayOf<Any>(src))
            val getAttributeInt = exifClass.getMethod(
                "getAttributeInt", *arrayOf(
                    String::class.java,
                    Int::class.javaPrimitiveType
                )
            )
            val tagOrientationField = exifClass.getField("TAG_ORIENTATION")
            val tagOrientation = tagOrientationField[null] as String
            orientation =
                getAttributeInt.invoke(exifInstance, *arrayOf(tagOrientation, 1)) as Int
        }
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: java.lang.IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }
    return orientation
}


fun encrypt_string(text: String, takeLast: String): String {
    var encrypted = ""
    val decrypted = ""
    var iv = ""
    try {
        Log.e("------", "---text---$takeLast")
        Log.e("------", "---text.substring(1,5)---" + text.substring(2, 6))
        val crypt = CryptLibrary()
        val key: String =
            CryptLibrary.SHA256("BeutifulPiscataway08854", 32) //32 bytes = 256 bit
        iv = "2a32b9fa76a4" + takeLast
        //iv = "2a32b9fa76a451a4";
        encrypted = crypt.encrypt(text, key, iv)
        //encrypt
        Log.e("------", "---encrypted---$encrypted")

        val dec = crypt.decrypt(encrypted, key, iv)
        Log.e("------", "---decrypt---$dec")


        //          Toast.makeText(Login.this, "encrypted text=" + encrypted, Toast.LENGTH_SHORT).show();
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return encrypted
}

fun rotateBitmap(
    src: String,
    bitmap: Bitmap
): Bitmap? {
    try {
        val orientation: Int = getExifOrientation(src!!)
        if (orientation == 1) {
            return bitmap
        }
        val matrix = Matrix()
        when (orientation) {
            2 -> matrix.setScale(-1f, 1f)
            3 -> matrix.setRotate(180f)
            4 -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            5 -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            6 -> matrix.setRotate(90f)
            7 -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            8 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val oriented = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
            bitmap.recycle()
            oriented
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            bitmap
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}




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





fun uploadFile(
    uploadRequest: UploadDataRequest,
    imgpath: String,
    doccode: String,
    applicationContext: Context
) {
    Log.e("Request---", uploadRequest.toString())

    val file = File(uploadRequest.imgstr)
    //  if (file.exists()) {
    val imgString = getBase64ImgString(file)
    uploadRequest.imgstr = imgString

    if (uploadRequest.doccode == "img") {
        uploadRequest.txtype = UPLOADIMG
        if (uploadRequest.pickup == "P") {
            uploadRequest.docname =  "Pickup Photo"
        } else if (uploadRequest.pickup == "D") {
            uploadRequest.docname = "Delivery Photo"
        }
    } else if (doccode == "rec" || doccode == "norec") {
        uploadRequest.txtype = UPLOADPOD
        if (uploadRequest.pickup == "P") {
            uploadRequest.docname = "BOL - Pickup(Image)"
        } else if (uploadRequest.pickup == "D") {
            uploadRequest.docname = "POD - Receipt(Image)"
        }
    }

    ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
        .upload(uploadRequest)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            //  mView.handlePickupListSuccessNew(it[0])
            handleUploadResponse(it[0], imgpath, doccode, applicationContext)
        }, {
            //   mView.hideProgress()
            // mView.handleLoginFailure(it.getErrorMessage(mContext))
        })
}



fun UploadDelSign(request: UploadSignRequest, context: Context){
    Log.e("Test.....1", "UploadDelSign")
    if (context != null) OnDataLoaded = context as onDataLoaded

    val dbhelper = DBHelper(context)
    val db = dbhelper.writableDatabase
    var newValues = ContentValues()
    var newShipmentValues = ContentValues()
    val contentValues = ContentValues()
    ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
        .uploadDelSign(request)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

            Log.e("Trivenee----", "Upload sign response")

            newValues = ContentValues()
            newShipmentValues = ContentValues()
            val siguploaddate =
                SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    .format(Date())

            val retval = getToken(it.toString())
            val sbols = it[0].sbolnum
            val token = it[0].txnum

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
                    "img",
                    newValues,
                    "type = 'sig' and sbolnum = ?",
                    arrayOf(sbol)
                )
                db.update(
                    "img",
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
            uploadSignedPdf(token, "", context)
            //   handleUploadResponse(it[0], "", "")


        }, {

        })


}
//retuen path in save sign response

fun SaveSignPDFAPI(pickupResponse: PickupResponse, token: String, context: Context) {
    //api call
    Log.e("Output--", "SaveSignPDFAPI")
    val req1 = UploadSignRequest(
        getDeviceId(), getToken(), "P", "dpod", "", "", "", "", "", "",
        "", "", "", "", "", "", "",
        getTimestamp(), "", "", ""
    )

    var req=SaveSignPdfRequest(
        getDeviceId(),
        getToken(),
        "P",
        "dpod",
        token,
        pickupResponse.txnum2,
        getTimestamp()
    )

    ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
        .saveSignPDF(req)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

            try {
                Log.e("Trivenee--12345--", "Upload sign response")

                var path = saveNewSignedPdf(it[0], token, context)
                uploadSignedPdf(token, it[0].txnum2, context)
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
            }

        }
        )

    //val s  =saveNewSignedPdf()
    //apenend
    //saveSighnpdf(token ,txnum2

}

fun saveFiles(jsonObject: PickupResponse) {
    Log.e("----------","saveFile")
    try {
        val filename =
            java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
//        val jsonArray = JSONArray(jsonStr)
//        val jsonObject = jsonArray.getJSONObject(0)
        var filestr = ""
        try {
            filestr = jsonObject.imgstr!!
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
        val sbolnum = jsonObject.sbolnum
        val txdate = jsonObject.txdate
        val shipdate = jsonObject.shipdate
        val srclocation = jsonObject.srclocation
        val doccode = jsonObject.doccode
        val picknum = jsonObject.picknum
        val delvnum = jsonObject.delvnum
        val bolnum = jsonObject.bolnum
        val shipnum = jsonObject.shipnum
        val shipper = jsonObject.shipper
        val shipadd1 = jsonObject.shipadd1
        val shipadd2 = jsonObject.shipadd2
        val shipcity = jsonObject.shipcity
        val shipstate = jsonObject.shipstate
        val shipzip = jsonObject.shipzip
        val shipcntry = jsonObject.shipcntry
        val shipacc = jsonObject.shipacc
        val consignee = jsonObject.consignee
        val consadd1 = jsonObject.consadd1
        val consadd2 = jsonObject.consadd2
        val conscity = jsonObject.conscity
        val consstate = jsonObject.consstate
        val conszip = jsonObject.conszip
        val conscntry = jsonObject.conscntry
        val consattn = jsonObject.consattn
        val consacc = jsonObject.consacc
        val hazmat = jsonObject.hazmat
        val hmcontact = jsonObject.hmcontact
        val spinstr = jsonObject.spinstr
        val stopnum = jsonObject.stopnum
        val pronum = jsonObject.pronum
        val pickup = jsonObject.pickup
        val comments = ""
        val downloaddate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val mydb = DBHelper(context)
        mydb.addUnsignedBOL("pdf", sbolnum, bolnum, downloaddate, pdfpath, picknum, "n")

        if (pickup == "D") {
            //getOtherDeliveries(picknum, sbolnum)
        } else if (pickup == "P") {
           // getOtherPickups(picknum, sbolnum)
        }
    } catch (e: java.lang.Exception) {
        val err = e.message
    }
}
fun saveNewSignedPdf(jsonStr: PickupResponse, token: String, context: Context) :String{
    try {

        Log.e("Output--", "saveSignedPdf")
            val txnum2 =jsonStr.txnum2
            //String pickup = jsonObject.getString("pickup");
            val shipnum = jsonStr.shipnum
            val bolnum = jsonStr.bolnum
            val filename = token + "_" + bolnum + "_" + shipnum + ".pdf"
            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val folder = File(extStorageDirectory, "/SmartBOL/Completed BOL/")
            if (!folder.exists()) if (!folder.mkdirs()) {
                val i = 0
            }
            val f = File(folder, filename)
            val newpdfpath = f.absolutePath
            val filestr = jsonStr.imgstr
            val byts: ByteArray = getBytsFromBase64String(filestr)!!
            val fos = FileOutputStream(f)
            fos.write(byts)
            fos.close()
            val newdownloaddate = getTimestamp()
        try{
            val dbhelper = DBHelper(context)
            val db: SQLiteDatabase = dbhelper.getWritableDatabase()
        }catch (e: Exception){
            Log.e("exceptipon", e.toString())
        }

        val dbhelper = DBHelper(context)
        val db: SQLiteDatabase = dbhelper.getWritableDatabase()


         //   val dbhelper = DBHelper(context)
          //  val db = dbhelper.writableDatabase
            val sbolnum = dbhelper.getSbolNum(bolnum!!, shipnum!!)
            val picknum = dbhelper.getPickNum(sbolnum!!)
            val newValues = ContentValues()
            newValues.put("type", "pdf")
            newValues.put("path", newpdfpath)
            newValues.put("timestamp", newdownloaddate)
            newValues.put("sbolnum", sbolnum)
            newValues.put("picknum", picknum)
            // newValues.put("pickup", pickup);
            newValues.put("token", token)
            val bool = db.insert("img", null, newValues)
            val newShipmentValues = ContentValues()
            newShipmentValues.put("status", "yyy")
          //  newShipmentValues.put("path", newpdfpath)
            db.update(
                "shipments",
                newShipmentValues,
                "sbolnum = ? AND token = ?",
                arrayOf<String?>(sbolnum, token)
            )


        return newpdfpath

    //else if (onDataLoaded != null) onDataLoaded.pdfLoaded(token)
    } catch (e: java.lang.Exception) {
        val x = e.message
        return "null"
    }
}
fun uploadSignedPdf(token: String?, txnum: String?, context: Context) {
    try {
        Log.e("Output--", "uploadSignedPdf")

        var req=SaveSignPdfRequest(
            getDeviceId(),
            getToken(),
            "P",
            "dpod",
            token,
            txnum,
            getTimestamp()
        )
        ThisRetrofit.createUnAuthService(MasterAPIService::class.java)
            .saveSignPDF(req)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                var path = saveNewSignedPdf(it[0], token!!, context)
                //Append path
                SaveSignPDFAPI(it[0], token, context)
                Log.e("Path", path)
                // saveSignedPdf(it[0],token)
                Log.e("Output--", it[0].toString())


            }, {

            })


        //else if (onDataLoaded != null) onDataLoaded.pdfLoaded(token)
    } catch (e: Exception) {
        val x = e.message
    }
}

fun savePdfApi(pickupResponse: PickupResponse, token: String) {

   // uploadSignedPdf(pickupResponse.txnum2, token)
}

fun saveFiles(jsonStr: PickupResponse, pickupnum: String?) {
    try {
        val filename =
            java.lang.Double.toString(System.currentTimeMillis() * Math.random() * 100000000) + ".pdf"
       // val jsonArray = JSONArray(jsonStr)
     //   val jsonObject = jsonArray.getJSONObject(0)
        var filestr = ""
        try {
            filestr = jsonStr.imgstr!!
        } catch (e: java.lang.Exception) {
        }
        var pdfpath = ""
        if (filestr.length > 0) {
            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val folder = File(extStorageDirectory, "/SmartBOL/Pending BOL/")
            if (!folder.exists()) if (!folder.mkdirs()) {
                val i = 0
            }
            val f = File(folder, filename)
            pdfpath = f.absolutePath
            val byts: ByteArray = getBytsFromBase64String(filestr)!!
            val fos = FileOutputStream(f)
            fos.write(byts)
            fos.close()
        }
        val sbolnum = jsonStr.sbolnum
        val txdate = jsonStr.txdate
        val shipdate = jsonStr.shipdate
        val srclocation = jsonStr.srclocation
        val doccode = jsonStr.doccode
        val picknum = jsonStr.pickup
        val delvnum = jsonStr.delvnum
        val bolnum = jsonStr.bolnum
        val shipnum = jsonStr.shipnum
        val shipper = jsonStr.shipper
        val shipadd1 = jsonStr.shipadd1
        val shipadd2 = jsonStr.shipadd2
        val shipcity = jsonStr.shipcity
        val shipstate = jsonStr.shipstate
        val shipzip = jsonStr.shipzip
        val shipcntry = jsonStr.shipcntry
        val shipacc = jsonStr.shipacc
        val consignee = jsonStr.consignee
        val consadd1 = jsonStr.consadd1
        val consadd2 = jsonStr.consadd2
        val conscity = jsonStr.conscity
        val consstate = jsonStr.consstate
        val conszip = jsonStr.conszip
        val conscntry = jsonStr.conscntry
        val consattn = jsonStr.consattn
        val consacc = jsonStr.consacc
        val hazmat = jsonStr.hazmat
        val hmcontact = jsonStr.hmcontact
        val spinstr = jsonStr.spinstr
        val stopnum = jsonStr.stopnum
        val pronum = jsonStr.pronum
        val pickup = jsonStr.pickup
        val comments = ""
        val downloaddate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val mydb = DBHelper(context)
        mydb.addUnsignedBOL("pdf", sbolnum, bolnum, downloaddate, pdfpath, picknum, "n")
        if (mydb.countSBOLNum(sbolnum!!) === 0) {
           mydb.addShipment(
                sbolnum, txdate, shipdate,
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
           // Toast.makeText(SmartBOLDriver.applicationContext(),"Already scanned", Toast.LENGTH_LONG).show()
            mydb.updateShipment(
                sbolnum!!,
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
           // getOtherDeliveries(picknum, sbolnum)
        } else if (pickup == "P") {
            //getOtherPickups(picknum, sbolnum)
        }
    } catch (e: java.lang.Exception) {
        val err = e.message
    }
}

fun handleUploadResponse(
    response: PickupResponse,
    imgpath: String,
    doccode: String,
    context: Context
){

    val dbhelper = DBHelper(context)
    val db = dbhelper.writableDatabase
    val newValues = ContentValues()
    val newShipmentValues = ContentValues()
    val contentValues = ContentValues()
    val newValues1 = ContentValues()
if(response.txtype.equals("uppod")){
    val picknum: String = response.picknum!!
    val sbolnum: String = response.sbolnum!!

    if (response.retcode!! == 0) {
        val uploaddate =
            SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(Date())
        if (doccode == "rec") {
            //newShipmentValues.put("path", imgpath)
            newShipmentValues.put("downloaddate", uploaddate)
            newShipmentValues.put("status", "yyy")
            val i: Int = db.update(
                "shipments",
                newShipmentValues,
                "sbolnum = ? ",
                arrayOf(sbolnum)
            )

            newValues1.put("status", "y")

            db.update(
                "img",
                newValues1,
                "type = 'pdf' and sbolnum = ?",
                arrayOf(sbolnum)
            )
        } else if (doccode == "norec") {
            newShipmentValues.put("sbolnum", sbolnum)
            newShipmentValues.put("picknum", picknum)
            newShipmentValues.put("downloaddate", uploaddate)
            newShipmentValues.put("status", "yyy")
            db.update("shipments", newShipmentValues, "path = ? ", arrayOf<String>(imgpath))
        }
        contentValues.put("sbolnum", sbolnum)
        contentValues.put("picknum", picknum)
        contentValues.put("uploadtimestamp", uploaddate)
        contentValues.put("status", "y")
        db.update("img", contentValues, "path = ? ", arrayOf<String>(imgpath))
//        if (onDataLoaded != null) onDataLoaded.dataLoaded("abc")
//        if (onDataLoaded != null) onDataLoaded.pdfLoaded(var1)
    }
}
    else if(response.txtype.equals("udimg")){
    val uploaddate =
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            .format(Date())
    contentValues.put("uploadtimestamp", uploaddate)
    contentValues.put("status", "y")
    db.update("img", contentValues, "path = ? ", arrayOf<String>(imgpath))

}


}

fun getBase64ImgString(file: File): String? { // String filename) {
    var retString: String? = ""

    val file_size = java.lang.String.valueOf(file.length() / 1024).toInt()

    Log.e("File Size", file_size.toString())
    if (file.exists()) {
        try {
            val inputStream: InputStream = FileInputStream(file)
            var bytes: ByteArray
            val buffer = ByteArray(10)
            var bytesRead: Int
            val output = ByteArrayOutputStream()
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
            retString = Base64.encodeToString(
                output.toByteArray(),
                Base64.DEFAULT
            )
        } catch (e: java.lang.Exception) {
            val x = e.message
            e.printStackTrace()
        }
    }
    return retString
}

fun getBytsFromBase64String(b64str: String?): ByteArray? {
    return Base64.decode(b64str, Base64.DEFAULT)
}

const val DOCCODE = "doccode"
const val DOCTYPE = "doctype"
const val RETCODE = "retcode"

const val FLDACC = "account"
const val FILETYPE = "filetype"
const val FLDIMGSTR = "imgstr"
const val FLDIMGSTR2 = "imgstr2"
const val FLDFILENAME = "filename"
const val FLDTRAILER = "trailer"
const val FLDSHIPNUM = "shipnum"
const val FLDBOLNUM = "bolnum"
const val FLDCOMMENT = "comments"

const val TXDATE = "txdate"
const val SBOLNUM = "sbolnum"
const val SRCLOC = "srclocation"
const val SHIPDATE = "shipdate"
const val SHIPPER = "shipper"
const val SHIPADD1 = "shipadd1"
const val SHIPADD2 = "shipadd2"
const val SHIPCITY = "shipcity"
const val SHIPSTATE = "shipstate"
const val SHIPZIP = "shipzip"
const val SHIPCNTRY = "shipcntry"
const val SHIPACC = "shipacc"
const val CONSIGNEE = "consignee"
const val CONSADD1 = "consadd1"
const val CONSADD2 = "consadd2"
const val CONSCITY = "conscity"
const val CONSSTATE = "consstate"
const val CONSZIP = "conszip"
const val CONSCNTRY = "conscntry"
const val CONSACC = "consacc"
const val CONSATTN = "consattn"
const val HAZMAT = "hazmat"
const val HMCONTACT = "hmcontact"
const val SPINSTR = "spinstr"
const val STOPNUM = "stopnum"
const val SIGNEDBY = "signedby"
const val SIGNEREMAIL = "email"
const val SIGNGPS = "signgps"
const val SIGNTIME = "signtime"
const val SIGNHASH = "signhash"
const val SIGNINFO = "signinfo"
const val SIGNNOTES = "signnotes"
const val DELVNUM = "delvnum"
const val PICKNUM = "picknum"
const val PRONUM = "pronum"

const val LONGITUDE = "longitude"
const val LATITUDE = "latitude"

const val AUTHENTICATE = "auth"
const val SENDER = "user"
const val SENDERPASS = "pass"
const val TXTYPE = "txtype"
const val TXSTATUS = "txstatus"
const val TXNUM = "txnum"
const val UPLOADDELV = "udlv"
const val UPLOADIMG = "udimg"
const val DOWNLOADDELV = "ddlv"
const val DOWNLOADPOD = "dpod"
const val UPLOADPOD = "uppod"
const val UPLOADGPS = "ugps"
const val PICKUP = "pickup"








