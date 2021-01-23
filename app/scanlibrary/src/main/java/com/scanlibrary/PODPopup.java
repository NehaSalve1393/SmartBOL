/*
package com.scanlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class PODPopup  extends Activity {
    static EditText et_cons;
    static EditText et_bol;
    static EditText et_pro;
    static EditText et_trailer;
    static EditText et_comments;

    RadioButton noexceptionRB;
    RadioButton yesexceptionRB;
    Button uploadbtn;
    private Button btn_bol_barcode;
    private Button btn_pro_barcode;
    private Button btn_trailer_barcode;

    LinearLayout conslayout;
    LinearLayout bollayout;
    LinearLayout prolayout;
    LinearLayout trailerlayout;

    String uri;
    String sbolnum;
    String picknum;
    String type;
    String doccode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pod_popup);

        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        sbolnum = intent.getStringExtra("sbol");
        picknum = intent.getStringExtra("picknum");
        type = intent.getStringExtra("type");
        doccode = intent.getStringExtra("doccode");

        conslayout = (LinearLayout) findViewById(R.id.conslayout);
        bollayout = (LinearLayout) findViewById(R.id.bollayout);
        prolayout = (LinearLayout) findViewById(R.id.prolayout);
        trailerlayout = (LinearLayout) findViewById(R.id.trailerlayout);
        noexceptionRB = (RadioButton) findViewById(R.id.noexceptionRB);
        yesexceptionRB = (RadioButton) findViewById(R.id.yesexceptionRB);


        if (sbolnum == null || sbolnum.equals("")) {
            conslayout.setVisibility(View.VISIBLE);
            bollayout.setVisibility(View.VISIBLE);
            prolayout.setVisibility(View.VISIBLE);
            trailerlayout.setVisibility(View.VISIBLE);
            noexceptionRB.setVisibility(View.VISIBLE);
            yesexceptionRB.setVisibility(View.VISIBLE);

        } else {
            conslayout.setVisibility(View.GONE);
            bollayout.setVisibility(View.GONE);
            prolayout.setVisibility(View.GONE);
            trailerlayout.setVisibility(View.GONE);

            if(type.equals("pod"))
            {
                noexceptionRB.setVisibility(View.VISIBLE);
                yesexceptionRB.setVisibility(View.VISIBLE);
            }
            else{
                noexceptionRB.setVisibility(View.GONE);
                yesexceptionRB.setVisibility(View.GONE);
            }
        }

        et_cons = (EditText) findViewById(R.id.et_cons);
        et_bol = (EditText) findViewById(R.id.et_bol);
        et_pro = (EditText) findViewById(R.id.et_pro);
        et_trailer = (EditText) findViewById(R.id.et_trailer);
        et_comments = (EditText) findViewById(R.id.et_comments);


        btn_bol_barcode = (Button) findViewById(R.id.btn_bol_barcode);
        btn_pro_barcode = (Button) findViewById(R.id.btn_pro_barcode);
        btn_trailer_barcode = (Button) findViewById(R.id.btn_trailer_barcode);
        uploadbtn = (Button) findViewById(R.id.btn_upload);


        btn_bol_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Popup.this, geBarcodeReader.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        btn_pro_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Popup.this, BarcodeReader.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        btn_trailer_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Popup.this, BarcodeReader.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cons = et_cons.getText().toString();
                String bol = et_bol.getText().toString();
                String pro = et_pro.getText().toString();
                String trailer = et_trailer.getText().toString();
                String comments = et_comments.getText().toString();
                if (sbolnum == null || sbolnum.equals("")) {
                    if (cons.length() > 0 && bol.length() > 0) {
                        if (noexceptionRB.isChecked() || yesexceptionRB.isChecked()) {
                            if (yesexceptionRB.isChecked()) {
                                if (comments.length() > 0) {
                                    returnData();
                                } else {
                                    Toast.makeText(Popup.this, "Please describe issue in comments box", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                returnData();
                            }
                        } else {
                            Toast.makeText(Popup.this, "Please confirm if issue with delivery", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Popup.this, "Consignee name and BOL # are required", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(type.equals("pod")){
                        if (noexceptionRB.isChecked() || yesexceptionRB.isChecked()) {
                            if (yesexceptionRB.isChecked()) {
                                if (comments.length() > 0) {
                                    returnData();
                                } else {
                                    Toast.makeText(Popup.this, "Please describe issue in comments box", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                returnData();
                            }
                        } else {
                            Toast.makeText(Popup.this, "Please confirm if issue with delivery", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        returnData();
                    }
                }
            }
        });
    }

    public void onRadioButtonClicked(View v) {
        noexceptionRB = (RadioButton) findViewById(R.id.noexceptionRB);
        yesexceptionRB = (RadioButton) findViewById(R.id.yesexceptionRB);

        switch (v.getId()) {
            case R.id.noexceptionRB:

                noexceptionRB.setChecked(true);
                if (yesexceptionRB.isChecked()) {
                    yesexceptionRB.setChecked(false);
                }
                break;
            case R.id.yesexceptionRB:
                yesexceptionRB.setChecked(true);
                if (noexceptionRB.isChecked()) {
                    noexceptionRB.setChecked(false);
                }
                break;
        }
    }


    public static void setProNumber(String input) {
        et_pro.setText(input);
    }

    public static void setBolNumber(String input) {
        et_bol.setText(input);
    }

    public static void setTrailerNumber(String input) {
        et_trailer.setText(input);
    }

    public void returnData() {
        String cons = et_cons.getText().toString();
        String pro = et_pro.getText().toString();
        String trailer = et_trailer.getText().toString();
        String bol = et_bol.getText().toString();
        String comments = et_comments.getText().toString();
//        if (sbolnum == null || sbolnum.equals("")) {            //this is a image corresponding to a bol
//            if (pro.length() > 0) {
//                if (trailer.length() > 0 || bol.length() > 0) {
//
//                } else Toast.makeText(Popup.this, "Please enter a Trailer or BOL number", Toast.LENGTH_LONG).show();
//            } else Toast.makeText(Popup.this, "Please enter a Pro number", Toast.LENGTH_LONG).show();
//        }
        String exception = "";

        if (yesexceptionRB.isChecked()) {
            exception = "x";
        }
        if(noexceptionRB.isChecked()){
            exception = "";
        }

        Intent intent = new Intent();
        intent.putExtra("uri", uri);
        intent.putExtra("sbolnum", sbolnum);
        intent.putExtra("picknum", picknum);
        intent.putExtra("cons", cons);
        intent.putExtra("pro", pro);
        intent.putExtra("trailer", trailer);
        intent.putExtra("bol", bol);
        intent.putExtra("exception", exception);
        intent.putExtra("comments", comments);
        intent.putExtra("type", type);
        intent.putExtra("doccode", doccode);
        setResult(RESULT_OK, intent);

        finish();
    }
}
*/
