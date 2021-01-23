package com.scanlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jhansi on 28/03/15.
 */
public class ScanActivity extends Activity implements IScanner, ComponentCallbacks2 {
    String sbolnum;
    String picknum;
    String type;
    String doccode;
    String pickup;

    Uri finaluri;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if(doccode.equals("norec")) {
                super.onBackPressed();
                return;
            }else{
                ComponentName cn = null;
                if(pickup.equals("P"))
                    cn = new ComponentName(ScanActivity.this, "com.smartbol.mpod.PickupActivity");
                else if(pickup.equals("D"))
                    cn = new ComponentName(ScanActivity.this, "com.smartbol.mpod.DeliveryActivity");
                else
                    cn = new ComponentName(ScanActivity.this, "com.smartbol.mpod.MainMenu");

                Intent i = new Intent().setComponent(cn);

                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        sbolnum = i.getStringExtra("sbol");
        picknum = i.getStringExtra("picknum");
        type = i.getStringExtra("type");
        doccode = i.getStringExtra("doccode");
        pickup = i.getStringExtra("pickup");
        /*String finished = i.getStringExtra("finished");
        try {
            if (finished.equals("yes")) {
                ShowCroppedImage();
            }
        }catch (Exception e){

        }*/

        setContentView(R.layout.scan_layout);
        init();
    }

    private void init() {
        PickImageFragment fragment = new PickImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ScanConstants.OPEN_INTENT_PREFERENCE, getPreferenceContent());
        bundle.putString("sbol",sbolnum);
        bundle.putString("picknum",picknum);
        bundle.putString("type",type);
        bundle.putString("doccode",doccode);
        bundle.putString("pickup",pickup);
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    protected int getPreferenceContent() {
        return getIntent().getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);
    }

    //ORIGINAL CODE DO NOT DELETE YET
    @Override
    public void onBitmapSelect(Uri uri) {
        setFinalURI(uri);
        ScanFragment fragment = new ScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SELECTED_BITMAP, getFinalURI());
        bundle.putString("sbol",sbolnum);
        bundle.putString("picknum",picknum);
        bundle.putString("type",type);
        bundle.putString("doccode",doccode);
        bundle.putString("pickup",pickup);
        bundle.putString("finished","yes");
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        //fragmentTransaction.addToBackStack(ScanFragment.class.toString());
        fragmentTransaction.commit();

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
    @Override
    public void onScanFinish(Uri uri) {
        setFinalURI(uri);
        if (type.equals("pod")) {
            ResultFragment fragment = new ResultFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri);
            bundle.putString("sbol",sbolnum);
            bundle.putString("picknum",picknum);
            bundle.putString("type",type);
            bundle.putString("doccode",doccode);
            bundle.putString("pickup",pickup);

            fragment.setArguments(bundle);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, fragment);
            //  fragmentTransaction.addToBackStack(ResultFragment.class.toString());
            fragmentTransaction.commit();
        }
        else{
            //If picture of delivery open popup window
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
                        i.putExtra("pickup", pickup);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(i, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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

    public void setFinalURI(Uri uri) {
        this.finaluri = uri;
    }
    private Uri getFinalURI(){
        return this.finaluri;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String pro = data.getStringExtra("pro");       //Look into this regarding DB
                String trailer = data.getStringExtra("trailer");
                String bol = data.getStringExtra("bol");
                String exception = data.getStringExtra("exception");
                String sbolacc = data.getStringExtra("sbolacc");
                String comments = data.getStringExtra("comments");
                String cons = data.getStringExtra("cons");
                String conscity = data.getStringExtra("conscity");
                String consstate = data.getStringExtra("consstate");
//                String shipper = data.getStringExtra("shippername");
                String signedby = data.getStringExtra("signedby");
                String pickup = data.getStringExtra("pickup");


                Intent i = new Intent();
                i.putExtra("sbolnum", sbolnum);
                i.putExtra("picknum", picknum);
                i.putExtra("signedby", signedby);
                i.putExtra("conscity", conscity);
                i.putExtra("consstate", consstate);
                i.putExtra("type", type);
                i.putExtra("doccode", doccode);
                i.putExtra("cons", cons);
                i.putExtra("pro", pro);
                i.putExtra("trailer", trailer);
                i.putExtra("bol", bol);
                i.putExtra("exception", exception);
                i.putExtra("comments", comments);
                i.putExtra("sbolacc", sbolacc);
                i.putExtra("pickup", pickup);
                i.putExtra(ScanConstants.SCANNED_RESULT, getFinalURI());
                setResult(RESULT_OK, i);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }

        }
    }



    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                new AlertDialog.Builder(this)
                        .setTitle(R.string.low_memory)
                        .setMessage(R.string.low_memory_message)
                        .create()
                        .show();
                break;
            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    public String getSbolnum(){
        return this.sbolnum;
    }
    public String getPicknum(){
        return this.picknum;
    }
    public String getType(){
        return this.type;
    }
    public String getDoccode(){
        return this.doccode;
    }
    public String getPickup(){
        return this.pickup;
    }

    public native Bitmap getScannedBitmap(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    public native Bitmap getGrayBitmap(Bitmap bitmap);

    public native Bitmap getMagicColorBitmap(Bitmap bitmap);

    public native Bitmap getBWBitmap(Bitmap bitmap);

    public native float[] getPoints(Bitmap bitmap);

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("Scanner");
    }
}