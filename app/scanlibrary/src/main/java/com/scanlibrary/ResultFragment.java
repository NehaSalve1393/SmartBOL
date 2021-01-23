package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jhansi on 29/03/15.
 */
public class ResultFragment extends Fragment {

    private View view;
    private ImageView scannedImageView;
    private Button retakeButton;
    private Button doneButton;
    private Bitmap original;
    private Button originalButton;
    private Button MagicColorButton;
    private Button grayModeButton;
    private Button bwButton;
    private Bitmap transformed;
    Uri finaluri;

    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_layout, null);
        init();
        return view;
    }

    private void init() {
        scannedImageView = (ImageView) view.findViewById(R.id.scannedImage);
       /* originalButton = (Button) view.findViewById(R.id.original);
        originalButton.setOnClickListener(new OriginalButtonClickListener());
        MagicColorButton = (Button) view.findViewById(R.id.magicColor);
        MagicColorButton.setOnClickListener(new MagicColorButtonClickListener());
        grayModeButton = (Button) view.findViewById(R.id.grayMode);
        grayModeButton.setOnClickListener(new GrayButtonClickListener());
        bwButton = (Button) view.findViewById(R.id.BWMode);
        bwButton.setOnClickListener(new BWButtonClickListener());*/
        Bitmap bitmap = getBitmap();
        setScannedImage(bitmap);
        doneButton = (Button) view.findViewById(R.id.doneButton);
        retakeButton = (Button) view.findViewById(R.id.retakeButton);
        retakeButton.setOnClickListener(new RetakeButtonClickListener());
        doneButton.setOnClickListener(new DoneButtonClickListener());



    }

    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = Utils.getBitmap(getActivity(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return original;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanConstants.SCANNED_RESULT);
        return uri;
    }

    public void setScannedImage(Bitmap scannedImage) {
        scannedImageView.setImageBitmap(scannedImage);
    }
    private class RetakeButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int preference = ScanConstants.OPEN_CAMERA;
            Intent i = new Intent(getContext(), ScanActivity.class);


            getActivity().setContentView(R.layout.scan_layout);

            PickImageFragment fragment = new PickImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ScanConstants.OPEN_INTENT_PREFERENCE, getActivity().getIntent().getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA));
            bundle.putString("sbol", ((ScanActivity) getActivity()).getSbolnum());
            bundle.putString("picknum", ((ScanActivity) getActivity()).getPicknum());
            bundle.putString("type", ((ScanActivity) getActivity()).getType());
            bundle.putString("doccode", ((ScanActivity) getActivity()).getDoccode());
            bundle.putString("pickup", ((ScanActivity) getActivity()).getPickup());

            fragment.setArguments(bundle);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commit();
        }
    }
    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //     showProgressDialog(getResources().getString(R.string.loading));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent data = new Intent();
                        Bitmap bitmap = transformed;
                        if (bitmap == null) {
                            bitmap = original;
                        }
                        setFinalURI(Utils.getUri(getActivity(), bitmap));
                        getActivity().setResult(RESULT_OK, data);
                        //original.recycle();
                        System.gc();

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                ComponentName cn = new ComponentName(getActivity(), "com.smartbol.mpod.Popup");
                                Intent i = new Intent().setComponent(cn);
                                i.putExtra("sbol", ((ScanActivity) getActivity()).getSbolnum());
                                i.putExtra("picknum", ((ScanActivity) getActivity()).getPicknum());
                                i.putExtra("type", ((ScanActivity) getActivity()).getType());
                                i.putExtra("doccode", ((ScanActivity) getActivity()).getDoccode());
                                i.putExtra("pickup", ((ScanActivity) getActivity()).getPickup());
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivityForResult(i, 1);

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setFinalURI(Uri uri) {
        this.finaluri = uri;
    }
    private Uri getFinalURI(){
        return this.finaluri;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {



                String sbolnum =data.getStringExtra("sbolnum");
                String picknum =data.getStringExtra("picknum");
                String sbolacc =data.getStringExtra("sbolacc");
                String shipper =data.getStringExtra("shipper");
                String signedby =(data.getStringExtra("signedby"));
                String bol = data.getStringExtra("bol");
                String pro = data.getStringExtra("pro");       //Look into this regarding DB
                String trailer = data.getStringExtra("trailer");
                String exception =data.getStringExtra("exception");
                String comments = data.getStringExtra("comments");
                String cons = data.getStringExtra("cons");
                String conscity = data.getStringExtra("conscity");
                String consstate =data.getStringExtra("consstate");
                String pickup = data.getStringExtra("pickup");

                Intent i = new Intent();
                i.putExtra("sbolnum", sbolnum);
                i.putExtra("picknum", picknum);
                i.putExtra("signedby", signedby);
                i.putExtra("pro", pro);
                i.putExtra("bol", bol);
                i.putExtra("pickup", pickup);
                i.putExtra("cons", cons);
                i.putExtra("conscity", conscity);
                i.putExtra("consstate", consstate);
                i.putExtra("exception", exception);
                i.putExtra("comments", comments);
                i.putExtra("type", ((ScanActivity) getActivity()).getType());
                i.putExtra("doccode", ((ScanActivity) getActivity()).getDoccode());
                i.putExtra(ScanConstants.SCANNED_RESULT, getFinalURI());
                getActivity().setResult(RESULT_OK, i);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });

            }
        }
    }

/*    private class BWButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getBWBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    private class MagicColorButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    private class OriginalButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                showProgressDialog(getResources().getString(R.string.applying_filter));
                transformed = original;
                scannedImageView.setImageBitmap(original);
                dismissDialog();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                dismissDialog();
            }
        }
    }

    private class GrayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }*/


}