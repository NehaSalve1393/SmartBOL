package com.example.smartboldriver

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.smartboldriver.api.SmartBOL24Retrofit


class SmartBOLDriver : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
       // var localeManager: LocaleManager? = null
        private var instance: SmartBOLDriver? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
//        Fabric.with(this, Crashlytics(), Answers())
/*        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)*/
    }


    override fun attachBaseContext(base: Context) {
//        localeManager = LocaleManager(base)
        super.attachBaseContext(base)
        MultiDex.install(this)
//        Log.d(ContentValues.TAG, "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
       super.onConfigurationChanged(newConfig)
//        localeManager?.setLocale(this)
//        Log.d(ContentValues.TAG, "onConfigurationChanged: " + newConfig.locale.language)
    }
}