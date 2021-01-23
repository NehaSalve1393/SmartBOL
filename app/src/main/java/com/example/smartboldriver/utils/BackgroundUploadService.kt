package com.example.smartboldriver.utils

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

class BackgroundUploadService {

     public fun scheduleJob(context: Context) {
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
}