package com.example.smartboldriver.api

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.SmartBOLDriver

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object SmartBOL24Retrofit {
    var baseUrl:String? = "https://api.smartbol.com/api/txm.aspx/"
  //  var prodUrl:String? = "https://api.smartbol.com/api/txm.aspx"
    var apiUrl:String? = ""
    var sharedpreferences: SharedPreferences? = null

    private fun getAcceptHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Accept", "application/x-www-form-urlencoded")
                .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    fun <S> createService(
        serviceClass: Class<S>,
        getHeaders: ((request: Request) -> Request.Builder)? = null
    ): S {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addNetworkInterceptor(getAcceptHeaderInterceptor())

        getHeaders?.let {
            okHttpClientBuilder.addNetworkInterceptor(getAuthHeaderInterceptor(it))
        }


        val okHttpClient = okHttpClientBuilder.build()
        okHttpClient.dispatcher().maxRequests = 1
        okHttpClient.dispatcher().maxRequestsPerHost = 1


        var sharedpreferences = SmartBOLDriver.applicationContext().getSharedPreferences("SharedPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val testflag: String = sharedpreferences!!.getString("Test", "n")!!
        if (testflag == "y") {
            baseUrl = "https://cltest.smartbol.com/api3/"

        }
        else{
            baseUrl = "https://api.smartbol.com/api/txm.aspx/"
        }


        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(serviceClass)
    }


    private fun getAuthHeaderInterceptor(getHeaders: (request: Request) -> Request.Builder): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = getHeaders(originalRequest)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}