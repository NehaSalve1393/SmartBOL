package com.example.smartboldriver.View

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentCallbacks2
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.scanlibrary.*

class ScanActivity : Activity(), IScanner, ComponentCallbacks2 {
    private var sbolnum  = ""
    private var picknum = "null"
    private var type= "null"
    private var doccode = ""
    private  var pickup = ""

    var finaluri: Uri? = null
    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (doccode == "norec") {
                super.onBackPressed()
                return
            } else {
                var cn: ComponentName? = null
                cn = if (pickup == "P") ComponentName(
                    this@ScanActivity,
                    "com.smartbol.mpod.PickupActivity"
                ) else if (pickup == "D") ComponentName(
                    this@ScanActivity,
                    "com.smartbol.mpod.DeliveryActivity"
                ) else ComponentName(this@ScanActivity, "com.smartbol.mpod.MainMenu")
                val i = Intent().setComponent(cn)
                i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
            }
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            "Please click BACK again to exit",
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = intent
        sbolnum = i.getStringExtra("sbol")!!
        picknum = i.getStringExtra("picknum")!!
        type = i.getStringExtra("type")!!
        doccode = i.getStringExtra("doccode")!!
        pickup = i.getStringExtra("pickup")!!
        /*String finished = i.getStringExtra("finished");
        try {
            if (finished.equals("yes")) {
                ShowCroppedImage();
            }
        }catch (Exception e){

        }*/setContentView(R.layout.scan_layout)
        init()
    }

    private fun init() {
        val fragment = PickImageFragment()
        val bundle = Bundle()
        bundle.putInt(ScanConstants.OPEN_INTENT_PREFERENCE, getPreferenceContent())
        bundle.putString("sbol", sbolnum)
        bundle.putString("picknum", picknum)
        bundle.putString("type", type)
        bundle.putString("doccode", doccode)
        bundle.putString("pickup", pickup)
        fragment.arguments = bundle
        val fragmentManager = fragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content, fragment)
        fragmentTransaction.commit()
    }

    protected fun getPreferenceContent(): Int {
        return intent.getIntExtra(
            ScanConstants.OPEN_INTENT_PREFERENCE,
            ScanConstants.OPEN_CAMERA
        )
    }

    //ORIGINAL CODE DO NOT DELETE YET
    override fun onBitmapSelect(uri: Uri?) {
        setFinalURI(uri)
        val fragment = ScanFragment()
        val bundle = Bundle()
        bundle.putParcelable(ScanConstants.SELECTED_BITMAP, getFinalURI())
        bundle.putString("sbol", sbolnum)
        bundle.putString("picknum", picknum)
        bundle.putString("type", type)
        bundle.putString("doccode", doccode)
        bundle.putString("pickup", pickup)
        bundle.putString("finished", "yes")
        fragment.arguments = bundle
        val fragmentManager = fragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content, fragment)
        //fragmentTransaction.addToBackStack(ScanFragment.class.toString());
        fragmentTransaction.commit()
    }

    /*
        @Override
        public void onBitmapSelect(Uri uri) {
            //setFinalURI(uri);
            ResultFragment fragment = new ResultFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri);
            bundle.putString("sbol",sbolnum);
            bundle.putString("picknum",picknum);
            bundle.putString("type",type);
            bundle.putString("doccode",doccode);
            fragment.setArguments(bundle);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.addToBackStack(ResultFragment.class.toString());
            fragmentTransaction.commit();

        }
    */
    override fun onScanFinish(uri: Uri?) {
        setFinalURI(uri)
        if (type == "pod") {
            val fragment = ResultFragment()
            val bundle = Bundle()
            bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri)
            bundle.putString("sbol", sbolnum)
            bundle.putString("picknum", picknum)
            bundle.putString("type", type)
            bundle.putString("doccode", doccode)
            bundle.putString("pickup", pickup)
            fragment.arguments = bundle
            val fragmentManager = fragmentManager
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.content, fragment)
            //  fragmentTransaction.addToBackStack(ResultFragment.class.toString());
            fragmentTransaction.commit()
        } else {
            //If picture of delivery open popup window
            AsyncTask.execute {
                try {
                    val cn = ComponentName(
                        this@ScanActivity,
                        "com.example.smartboldriver.View.CapturePODFormActivity"
                    )
                    val i = Intent().setComponent(cn)
                    i.putExtra("sbol", sbolnum)
                    i.putExtra("picknum", picknum)
                    i.putExtra("type", type)
                    i.putExtra("doccode", doccode)
                    i.putExtra("pickup", pickup)
                    i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(i, 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
/*
 //ORIGINAL CODE DO NOT DELETE YET
    @Override
    public void onScanFinish(Uri uri) {
        setFinalURI(uri);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ComponentName cn = new ComponentName(ScanActivity.this, "com.smartbol.mpod.Popup");
                    Intent i = new Intent().setComponent(cn);
                    i.putExtra("sbol", sbolnum);
                    i.putExtra("picknum", picknum);
                    i.putExtra("type", type);
                    i.putExtra("doccode", doccode);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(i, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    }
*/

    /*
 //ORIGINAL CODE DO NOT DELETE YET
    @Override
    public void onScanFinish(Uri uri) {
        setFinalURI(uri);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ComponentName cn = new ComponentName(ScanActivity.this, "com.smartbol.mpod.Popup");
                    Intent i = new Intent().setComponent(cn);
                    i.putExtra("sbol", sbolnum);
                    i.putExtra("picknum", picknum);
                    i.putExtra("type", type);
                    i.putExtra("doccode", doccode);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(i, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    }
*/
    fun setFinalURI(uri: Uri?) {
        finaluri = uri
    }

    private fun getFinalURI(): Uri? {
        return finaluri
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val pro = data.getStringExtra("pro") //Look into this regarding DB
                val trailer = data.getStringExtra("trailer")
                val bol = data.getStringExtra("bol")
                val exception = data.getStringExtra("exception")
                val sbolacc = data.getStringExtra("sbolacc")
                val comments = data.getStringExtra("comments")
                val cons = data.getStringExtra("cons")
                val conscity = data.getStringExtra("conscity")
                val consstate = data.getStringExtra("consstate")
                //                String shipper = data.getStringExtra("shippername");
                val signedby = data.getStringExtra("signedby")
                val pickup = data.getStringExtra("pickup")
                val i = Intent()
                i.putExtra("sbolnum", sbolnum)
                i.putExtra("picknum", picknum)
                i.putExtra("signedby", signedby)
                i.putExtra("conscity", conscity)
                i.putExtra("consstate", consstate)
                i.putExtra("type", type)
                i.putExtra("doccode", doccode)
                i.putExtra("cons", cons)
                i.putExtra("pro", pro)
                i.putExtra("trailer", trailer)
                i.putExtra("bol", bol)
                i.putExtra("exception", exception)
                i.putExtra("comments", comments)
                i.putExtra("sbolacc", sbolacc)
                i.putExtra("pickup", pickup)
                i.putExtra(ScanConstants.SCANNED_RESULT, getFinalURI())
                setResult(Activity.RESULT_OK, i)
                runOnUiThread { finish() }
            }
        }
    }


    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE, ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW, ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND, ComponentCallbacks2.TRIM_MEMORY_MODERATE, ComponentCallbacks2.TRIM_MEMORY_COMPLETE ->                 /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */AlertDialog.Builder(this)
                .setTitle(R.string.low_memory)
                .setMessage(R.string.low_memory_message)
                .create()
                .show()
            else -> {
            }
        }
    }

    fun getSbolnum(): String? {
        return sbolnum
    }

    fun getPicknum(): String? {
        return picknum
    }

    fun getType(): String? {
        return type
    }

    fun getDoccode(): String? {
        return doccode
    }

    fun getPickup(): String? {
        return pickup
    }

    external fun getScannedBitmap(
        bitmap: Bitmap?,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float,
        x4: Float,
        y4: Float
    ): Bitmap?

    external fun getGrayBitmap(bitmap: Bitmap?): Bitmap?

    external fun getMagicColorBitmap(bitmap: Bitmap?): Bitmap?

    external fun getBWBitmap(bitmap: Bitmap?): Bitmap?

    external fun getPoints(bitmap: Bitmap?): FloatArray?

    companion object
    {
        init {
            System.loadLibrary("opencv_java3")
            System.loadLibrary("Scanner")
        }

    }
}