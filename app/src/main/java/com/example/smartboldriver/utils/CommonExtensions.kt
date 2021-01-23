package com.example.smartboldriver.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

//region: Context Extension
fun Context.isConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun Context.hasPermissions(permissions: Array<String>): Boolean{
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
    }
    return true
}


fun Context.getManifestPermissions(): Array<String> {
    var packageInfo: PackageInfo? = null
    val list = ArrayList<String>(1)
    try {
        packageInfo = packageManager.getPackageInfo(packageName,
            PackageManager.GET_PERMISSIONS)
    } catch (e: PackageManager.NameNotFoundException) {
        e.message?.let { error(it) }
    }

    if (packageInfo != null) {
        val permissions = packageInfo.requestedPermissions
        if (permissions != null) {
            Collections.addAll(list, *permissions)
        }
    }
    return list.toTypedArray()
}
//endregion

fun Context.getColorCompat(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * An Extension function that separates the items of array list into a new line
 *
 * @receiver ArrayList<String>
 * @return String
 */
fun ArrayList<String>.toLineSeparatedString(): String {
    val sb = StringBuilder("")
    this.forEach {
        sb.append("$it \n")
    }
    return sb.toString()
}



fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}



val EditText.value get() = text.toString()


fun <T : Any> T.TAG() = this::class.java.simpleName




