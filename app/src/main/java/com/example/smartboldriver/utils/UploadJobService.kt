package com.example.smartboldriver.utils

import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.example.smartboldriver.models.QueueObject

public class UploadJobService : JobService() {

    private val TAG = "UploadService"
    var context: Context = this
    var min = 1 //default value if user doesn't input time to update


    override fun onStartJob(params: JobParameters?): Boolean {
//        Toast.makeText(this, "Upload Service created!", Toast.LENGTH_LONG).show();
//        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
//        int min = 1;    //default value if user doesn't input time to update
//        String upload = "";
//        if (sharedPreferences.contains("Upload")) {
//            upload = (sharedPreferences.getString("Upload", ""));
//            if (upload.length() > 0)
//                min = Integer.parseInt(upload);
//        }
//        final int minutes = min;
        val uplTrans = ClsUplTrans(applicationContext)
        val mydb = DBHelper(this)
        try {

            val obj: List<QueueObject> = mydb.pendingImages()!!
            val size = obj.size
            //Toast.makeText(BackgroundService.this, String.valueOf(size), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "Upload Service is still running", Toast.LENGTH_SHORT).show();
            for (`object` in obj) {
                val type: String = `object`.type
                val sbolnum: String = `object`.sbolnum
                val comments: String = `object`.comment
                val gps: String = `object`.gps
                val picknum: String = `object`.picknum
                val timestamp: String = `object`.timestamp
                val signedby: String = `object`.signedby
                val pickup: String = `object`.pickup
                if (type == "sig") {
                    val email: String = `object`.email
                    val signimage: String = `object`.signimage
                    val signhash: String = `object`.signhash
                    val token: String = `object`.token
                    val exception: String = `object`.exception
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
                    val shipmentNum: String = `object`.shipment
                    val trailerNum: String = `object`.trailer
                    val bolNum: String = `object`.bol
                    val shipper: String = `object`.signedby
                    val cons: String = `object`.cons
                    val conscity: String = `object`.conscity
                    val consstate: String = `object`.consstate
                    val doccode: String = `object`.doccode
                    val exception: String = `object`.exception
                    val account: String = `object`.account
                    val pro: String = `object`.pro

                    val filename =filePath.substring(filePath.lastIndexOf("/") + 1)
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
                        shipper,
                        gps,
                        doccode,
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
                } else if (type == "pod") {
                    val filePath: String = `object`.path
                    val shipmentNum: String = `object`.shipment
                    val trailerNum: String = `object`.trailer
                    val bolNum: String = `object`.bol
                    val shipper: String = `object`.signedby
                    val cons: String = `object`.cons
                    val conscity: String = `object`.conscity
                    val consstate: String = `object`.consstate
                    val doccode: String = `object`.doccode
                    val exception: String = `object`.exception
                    val account: String = `object`.account
                    val pro: String = `object`.pro
                    val filename = filePath.substring(filePath.lastIndexOf("/") + 1)
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
                        shipper,
                        gps,
                        doccode,
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
                }
            }
        } catch (e: Exception) {
            val s = e.toString()
        }
        if (isAppRunning()) {
          //  BackgroundUploadService.scheduleJob(applicationContext) // reschedule the job
            scheduleJob(applicationContext) // reschedule the job
            //BackgroundUploadService.
        }
        return true
    }

     fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(
            context,
            UploadJobService::class.java
        )
        val builder =
            JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency(20 * 1000.toLong()) // wait at least
        //        builder.setOverrideDeadline(60 * 1000); // maximum delay
        val jobScheduler =
            context.getSystemService(
                JobScheduler::class.java
            )
        jobScheduler.schedule(builder.build())
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun isAppRunning(): Boolean {
        val m =
            this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfoList =
            m.getRunningTasks(100)
        val itr: Iterator<ActivityManager.RunningTaskInfo> =
            runningTaskInfoList.iterator()
        var n = 0
        while (itr.hasNext()) {
            n++
            itr.next()
        }
        return if (n == 1) { // App is killed
            //            Toast.makeText(UploadJobService.this, "Upload Killed", Toast.LENGTH_SHORT).show();
            false
        } else true
        //        Toast.makeText(UploadJobService.this, "Upload Alive", Toast.LENGTH_SHORT).show();
        // App is in background or foreground
    }
}