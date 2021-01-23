package com.example.smartboldriver.api

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log

object ThisRetrofit {
    fun <S> createBareService(serviceClass: Class<S>): S {
        return SmartBOL24Retrofit.createService(serviceClass)
    }

    fun <S> createUnAuthService(serviceClass: Class<S>): S {
        return SmartBOL24Retrofit.createService(serviceClass) { originalRequest ->
            Log.d("request-->",originalRequest.toString())
            originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body())

        }
    }


//    fun <S> createAuthService(serviceClass: Class<S>): S {
//        return SmartBOL24Retrofit.createService(serviceClass) { originalRequest ->
//            originalRequest.newBuilder()
//                .header(HEADER_DEVICE_ID, getAndroidId())
//                .header(HEADER_SESSION_TOKEN, getToken())
//                .header(HEADER_USER_TYPE, getUserType())
//                .method(originalRequest.method(), originalRequest.body())
//        }
//    }
//
//    private inline fun getFCMTokenAsync(crossinline tokenCallback: ((String) -> Unit)) {
//        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
//            tokenCallback(it.result?.token ?: "")
//        }
//    }


//    @SuppressLint("HardwareIds")
//    private fun getAndroidId(): String {
//        return try {
//            Settings.Secure.getString(Commo24App.applicationContext().contentResolver,
//                Settings.Secure.ANDROID_ID)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//    }
}