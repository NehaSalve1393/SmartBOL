package com.example.smartboldriver.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.UploadDataRequest

class BGService : Service() {
    var context: Context = this
    var handler: Handler? = null
    var runnable: Runnable? = null
    val PRIMARY_NOTIF_CHANNEL = "default"
    val PRIMARY_FOREGROUND_NOTIF_SERVICE_ID = 1001

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @SuppressLint("WrongConstant")
    override fun onCreate() {

        Log.e("Service----","BGService")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val chan1 = NotificationChannel(
                PRIMARY_NOTIF_CHANNEL,
                "default",
                NotificationManager.IMPORTANCE_NONE
            )
            chan1.lightColor = Color.TRANSPARENT
            chan1.lockscreenVisibility = Notification.VISIBILITY_SECRET
            notificationManager.createNotificationChannel(chan1)
            val notification: Notification = Notification.Builder(
                this,
                PRIMARY_NOTIF_CHANNEL
            ) //                    .setSmallIcon(com.smartbol.R.drawable.clear)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build()
            startForeground(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification)
        }
      //  Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show()
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var min = 3 //default value if user doesn't input time to update
        var upload: String? = ""
        if (sharedPreferences.contains("Upload")) {
            upload = sharedPreferences.getString("Upload", "")
            if (upload!!.length > 0) min = upload.toInt()
        }
        val minutes = min
        val uplTrans = ClsUplTrans(getApplicationContext())
        val mydb = DBHelper(this)
        handler = Handler()
        runnable = Runnable {
            try {
                var uploadingflag: String? = ""
                if (sharedPreferences.contains("UploadingFlag")) {
                    uploadingflag = sharedPreferences.getString("UploadingFlag", "")
                }
               // Toast.makeText(context, uploadingflag, Toast.LENGTH_SHORT).show()

                ///new
                uploadingflag = "n";
                if (uploadingflag == "n") {
                    editor.putString("UploadingFlag", "y")
                    editor.apply()

                    //  uplTrans.getDeliveries("",null);
                    //token = mydb.getProcessedBols();
                    //uplTrans.getSignedPdf(token,null);
                    val obj: List<QueueObject>? = mydb.pendingImages()
                    val size = obj!!.size
//                    Toast.makeText(
//                        context,
//                        "Service is still running _ " + Integer.toString(size),
//                        Toast.LENGTH_LONG
//                    ).show()
                    for (`object` in obj) {
                        val type: String = `object`.type
                        val sbolnum: String = `object`.sbolnum
                        val comments: String = `object`.comment
                        val gps: String = `object`.gps
                        val picknum: String = `object`.picknum
                        val doccode: String = `object`.doccode
                        val exception: String = `object`.exception
                        val timestamp: String = getTimestamp()!!
                        val pickup: String = `object`.pickup
                    //    Toast.makeText(context, "Uploading     $type", Toast.LENGTH_SHORT).show()
                        if (type == "sig") {
                            val signedby: String = `object`.signedby
                            val email: String = `object`.email
                            val signimage: String = `object`.signimage
                            val signhash: String = `object`.signhash
                            val token: String = `object`.token
                            uplTrans.uploadSignature(
                                signedby,
                                signimage,
                                signhash,
                                timestamp,
                                gps,
                                comments,
                                sbolnum,
                                email,
                                picknum,
                                exception,
                                pickup,
                                null
                            )
                        } else if (type == "img") {
                            val filePath: String = `object`.path
                            val filename = filePath.substring(filePath.lastIndexOf("/") + 1)
                            val shipmentNum: String = `object`.shipment
                            val trailerNum: String = `object`.trailer
                            val bolNum: String = `object`.bol
                            var uploadonwifionly: String? = ""
                            val account: String = `object`.account
                            val pro: String = `object`.pro
                            val cons: String = `object`.cons
                            val conscity: String = `object`.conscity
                            val consstate: String = `object`.consstate
                            val sharedpreferences: SharedPreferences =
                                getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
//                            if (sharedpreferences.contains("Wifi")) {
//                                uploadonwifionly = sharedpreferences.getString("Wifi", "")
//                            }
//                            val wifi =
//                                getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
//                            val wifiInfo = wifi.connectionInfo
//                            val ssid = wifiInfo.ssid
//                            if ( ssid != "<unknown ssid>") {

                                var req = UploadDataRequest(getDeviceId(), getToken(),"","img","","",filename,
                                    picknum,sbolnum,trailerNum,shipmentNum,bolNum,"",comments,"",gps,"","","",
                                    pro,"",conscity,consstate,pickup,filePath,"P",timestamp)
                                uploadFile(req,filePath,"pod",applicationContext)//Toast.makeText(MainMenu.this, "DO NOT Upload/Download", Toast.LENGTH_SHORT).show();

                                //not on wifi
//                                uplTrans.uploadFiles(
//                                    timestamp,
//                                    sbolnum,
//                                    filePath,
//                                    filename,
//                                    shipmentNum,
//                                    trailerNum,
//                                    bolNum,
//                                    exception,
//                                    comments,
//                                    "",
//                                    gps,
//                                    doccode,
//                                    picknum,
//                                    account,
//                                    pro,
//                                    cons,
//                                    conscity,
//                                    consstate,
//                                    pickup,
//                                    "",
//                                    null
//                                ) //Toast.makeText(MainMenu.this, "DO NOT Upload/Download", Toast.LENGTH_SHORT).show();
//                            } else if (uploadonwifionly == "n") {
//                                var req = UploadDataRequest(getDeviceId(), getToken(),"","img","","",filename,
//                                    picknum,sbolnum,trailerNum,shipmentNum,bolNum,"",comments,"",gps,"","","",
//                                    pro,"",conscity,consstate,pickup,filePath,account,timestamp)
//                                uploadFile(req,filePath,"pod")//Toast.makeText(MainMenu.this, "DO NOT Upload/Download", Toast.LENGTH_SHORT).show();


                                uplTrans.uploadFiles(
                                    timestamp,
                                    sbolnum,
                                    filePath,
                                    filename,
                                    shipmentNum,
                                    trailerNum,
                                    bolNum,
                                    exception,
                                    comments,
                                    "",
                                    gps,
                                    doccode,
                                    picknum,
                                    account,
                                    pro,
                                    cons,
                                    conscity,
                                    consstate,
                                    pickup,
                                    "",
                                    null
                                ) //Toast.makeText(MainMenu.this, "DO NOT Upload/Download", Toast.LENGTH_SHORT).show();
                           // }
                        } else if (type == "pod") {
                            val filePath: String = `object`.path
                          //  val filename = filePath.substring(filePath.lastIndexOf("/") + 1)
                            val filename = "filename"
                            val shipmentNum: String = `object`.shipment
                            val trailerNum: String = `object`.trailer
                            val bolNum: String = `object`.bol
                            val account: String = "P"
                            val pro: String = `object`.pro
                            val cons: String = `object`.cons
                            val conscity: String = `object`.conscity
                            val consstate: String = `object`.consstate
                            val signedby: String = `object`.signedby
                            uplTrans.uploadFiles(
                                timestamp, sbolnum, filePath, filename, shipmentNum,
                                trailerNum,
                                bolNum, exception, comments,
                                "",
                                gps, doccode,
                                picknum,
                                account,
                                pro,
                                cons,
                                conscity,
                                consstate,
                                pickup,
                                signedby,
                                null
                            )
                            var req = UploadDataRequest(getDeviceId(), getToken(),"",doccode,"","jpg",filename,
                            picknum,sbolnum,trailerNum,shipmentNum,bolNum,"y",comments,"",gps,"","","",
                            pro,cons,conscity,consstate,pickup,filePath,account,timestamp)
                            uploadFile(req, filePath, doccode, applicationContext)//Toast.makeText(MainMenu.this, "DO NOT Upload/Download", Toast.LENGTH_SHORT).show();
                        }
                    }
                    handler!!.postDelayed(runnable!!, 1000 * 60 * minutes.toLong())
                    editor.putString("UploadingFlag", "n")
                    editor.apply()
                    // Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }
            } catch (e: Exception) {
                val s = e.toString()
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
                editor.putString("UploadingFlag", "n")
                editor.apply()
            }
            //stopSelf();
        }
        handler!!.postDelayed(runnable!!, 1000)
    }


    override fun onDestroy() {
        handler!!.removeCallbacks(runnable!!)
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }

}