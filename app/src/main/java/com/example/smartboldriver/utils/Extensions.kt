package com.example.smartboldriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.smartboldriver.R
import com.google.android.material.textfield.TextInputLayout
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.DecimalFormat

fun Throwable.getErrorMessage(context: Context): String {
    if (this is HttpException) {
        if (this.code() == 401) {
          //  localLogout(context)
            return context.getString(R.string.error_msg_invalid_session)
        }
        return context.getString(R.string.error_msg_server_error)
    }
    if (this is ConnectException || this is SocketTimeoutException || this is UnknownHostException) {
        return if (context.isConnected()) {
            context.getString(R.string.error_msg_cannot_connect)
        } else {
            context.getString(R.string.error_msg_no_internet)
        }
    }
    Log.e("Throwable", "Exception", this)
    //Crashlytics.logException(this)
    return context.getString(R.string.error_msg_generic)
}

fun EditText.isEmpty(): Boolean = text.toString().isBlank()

fun EditText.value(): String = text.toString()

