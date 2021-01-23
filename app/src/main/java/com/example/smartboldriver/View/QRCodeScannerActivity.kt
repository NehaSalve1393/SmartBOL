package com.example.smartboldriver.View

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.R
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler


class SimpleScannerActivity :  AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)

        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume

    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {
        //
    }

//    override fun handleResult(rawResult: R) {
//        // Do something with the result here
//        // Log.v("tag", rawResult.getText()); // Prints scan results
//        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//
//       // DashBoardActivity.tvResult!!.setText(rawResult.text)
//        onBackPressed()
//
//        // If you would like to resume scanning, call this method below:
//        //mScannerView.resumeCameraPreview(this);
//    }
}