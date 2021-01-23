package com.example.smartboldriver.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.features.BarcodeScanner
import com.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.activity_p_o_d_details.*
import java.util.*

class PODDetailsActivity : AppCompatActivity() {
    var et_bol: EditText? = null
    var et_pro: EditText? = null

    var et_signer: EditText? = null
    var et_cons: EditText? = null
    var et_consstate: EditText? = null
    var et_conscity: EditText? = null
    var et_comments: EditText? = null

    var exceptionSpinner /*
    RadioButton noexceptionRB;
    RadioButton yesexceptionRB;*/: Spinner? = null
    var uploadbtn: Button? = null
    var btn_bol_barcode: Button? = null
    var btn_pro_barcode: Button? = null
    var delvbtn: Button? = null
    var pickupbtn: Button? = null

    var delvtypelayout: LinearLayout? = null
    var bollayout: LinearLayout? = null
    var prolayout: LinearLayout? = null
    var signerlayout: LinearLayout? = null
    var conslayout: LinearLayout? = null
    var shipadd2layout: LinearLayout? = null
    var conscitylayout: LinearLayout? = null

    var sbolnum: String? = null
    var picknum: String? = null
    var type: String? = null
    var doccode: String? = null
    var pickup: String? = ""
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_o_d_details)
        val intent = intent
        sbolnum = intent.getStringExtra("sbol")
        picknum = intent.getStringExtra("picknum")
        type = intent.getStringExtra("type")
        doccode = intent.getStringExtra("doccode")
        pickup = intent.getStringExtra("pickup")
        bollayout = findViewById<View>(R.id.bollayout) as LinearLayout
        delvtypelayout = findViewById<View>(R.id.delvtypelayout) as LinearLayout
        prolayout = findViewById<View>(R.id.prolayout) as LinearLayout
        signerlayout = findViewById<View>(R.id.signerlayout) as LinearLayout
        conslayout = findViewById<View>(R.id.conslayout) as LinearLayout
        conscitylayout = findViewById<View>(R.id.conscitylayout) as LinearLayout /*
        noexceptionRB = (RadioButton) findViewById(R.id.noexceptionRB);
        yesexceptionRB = (RadioButton) findViewById(R.id.yesexceptionRB);*/
        exceptionSpinner = findViewById<View>(R.id.exceptionSpinner) as Spinner
        val list: MutableList<String> = ArrayList()
        list.add("-- Select an option --")
        list.add("No issue with pickup/delivery")
        list.add("Short")
        list.add("Damaged")
        list.add("Refused")
        list.add("Other")
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, list
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exceptionSpinner!!.adapter = dataAdapter


        /*  if(pickup.equals("D")){
            noexceptionRB.setText("No issue with delivery");
            yesexceptionRB.setText("Issue with delivery (Short,Damaged...)");
        }else if(pickup.equals("P")){
            noexceptionRB.setText("No issue with pickup");
            yesexceptionRB.setText("Issue with pickup (Short,Damaged...)");
        }else{
            noexceptionRB.setText("No issue with pickup/delivery");
            yesexceptionRB.setText("Issue with pickup/delivery");
        }*/if (type == "img") {
            delvtypelayout!!.visibility = View.GONE
            bollayout!!.visibility = View.GONE
            prolayout!!.visibility = View.GONE
            signerlayout!!.visibility = View.GONE
            conslayout!!.visibility = View.GONE
            conscitylayout!!.visibility = View.GONE
            exceptionSpinner!!.visibility = View.GONE
        } else if (sbolnum == null || sbolnum == "") {
            delvtypelayout!!.visibility = View.VISIBLE
            bollayout!!.visibility = View.VISIBLE
            prolayout!!.visibility = View.VISIBLE
            signerlayout!!.visibility = View.VISIBLE
            conslayout!!.visibility = View.VISIBLE
            conscitylayout!!.visibility = View.VISIBLE /*
            noexceptionRB.setVisibility(View.VISIBLE);
            yesexceptionRB.setVisibility(View.VISIBLE);*/
            exceptionSpinner!!.visibility = View.VISIBLE
        } else {
            delvtypelayout!!.visibility = View.GONE
            bollayout!!.visibility = View.GONE
            prolayout!!.visibility = View.GONE
            conslayout!!.visibility = View.GONE
            conscitylayout!!.visibility = View.GONE
            if (type == "pod") {
                signerlayout!!.visibility = View.VISIBLE
                exceptionSpinner!!.visibility = View.VISIBLE /*
                noexceptionRB.setVisibility(View.VISIBLE);
                yesexceptionRB.setVisibility(View.VISIBLE);*/
            } else {
                signerlayout!!.visibility = View.GONE
                exceptionSpinner!!.visibility = View.GONE /*
                noexceptionRB.setVisibility(View.GONE);
                yesexceptionRB.setVisibility(View.GONE);*/
            }
        }
        et_bol = findViewById<View>(R.id.et_bol1) as EditText
        et_pro = findViewById<View>(R.id.et_pro1) as EditText
        et_signer = findViewById<View>(R.id.et_signer) as EditText
        et_cons = findViewById<View>(R.id.et_cons) as EditText
        et_conscity = findViewById<View>(R.id.et_conscity) as EditText
        et_consstate = findViewById<View>(R.id.et_consstate) as EditText
        et_comments = findViewById<View>(R.id.et_comments) as EditText
        delvbtn = findViewById<View>(R.id.delvbtn) as Button
        pickupbtn = findViewById<View>(R.id.pickupbtn) as Button
        btn_bol_barcode = findViewById<View>(R.id.btn_bol_barcode) as Button
        btn_pro_barcode = findViewById<View>(R.id.btn_pro_barcode) as Button
        uploadbtn = findViewById<View>(R.id.btn_upload) as Button
        delvbtn!!.setOnClickListener {
            delvbtn!!.setTextColor(Color.BLACK)
            pickupbtn!!.setTextColor(Color.GRAY) /*
                noexceptionRB.setText("No issue with delivery");
                yesexceptionRB.setText("Issue with delivery (Short,Damaged...)");*/
        }
        pickupbtn!!.setOnClickListener {
            pickupbtn!!.setTextColor(Color.BLACK)
            delvbtn!!.setTextColor(Color.GRAY) /*
                    noexceptionRB.setText("No issue with pickup");
                    yesexceptionRB.setText("Issue with pickup (Short,Damaged...)");*/
        }
        btn_bol_barcode!!.setOnClickListener {
            val intent: Intent =
                Intent(this, BarcodeScanner::class.java)
            intent.putExtra("type", 1)
            startActivityForResult(intent,1)
        }
        btn_pro_barcode!!.setOnClickListener {
            val intent: Intent =
                Intent(this, BarcodeScanner::class.java)
            intent.putExtra("type", 2)
            startActivityForResult(intent,1)
        }
        uploadbtn!!.setOnClickListener {
            val comments = et_comments!!.text.toString()
            if (delvbtn!!.currentTextColor == Color.BLACK || pickupbtn!!.currentTextColor == Color.BLACK || delvtypelayout!!.visibility == View.GONE) {
                if (et_signer!!.text.toString().length > 0 || signerlayout!!.visibility == View.GONE) {
                    if (et_cons!!.text.toString().length > 0 || conslayout!!.visibility == View.GONE) {
                        if (exceptionSpinner!!.visibility == View.GONE) {
                            returnData()
                        } else if (exceptionSpinner!!.selectedItem.toString() != "-- Select an option --") {
                            if (exceptionSpinner!!.selectedItem.toString() != "No issue with pickup/delivery") {
                                if (comments.length > 0) {
                                    returnData()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Please describe issue in comments box",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                returnData()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Please confirm if issue with delivery",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Please Enter Consignee's Company Name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Please Enter Signer's Name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please Select Delivery or Pickup",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
/*

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
*/


    /*

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
*/
    fun setProNumber(input: String?) {
        et_pro!!.setText(input)
    }

    fun setBolNumber(input: String?) {
        et_bol!!.setText(input)
    }


    fun returnData() {
        val signedby = et_signer!!.text.toString()
        val bol = et_bol!!.text.toString()
        val pro = et_pro!!.text.toString()
        val consignee = et_cons!!.text.toString()
        val consigneecity = et_conscity!!.text.toString()
        val consigneestate = et_consstate!!.text.toString()
        val comments = et_comments!!.text.toString()
        var exception = ""
        if (exceptionSpinner!!.selectedItem.toString() == "Short") exception =
            "S" else if (exceptionSpinner!!.selectedItem.toString() == "Damaged") exception =
            "D" else if (exceptionSpinner!!.selectedItem.toString() == "Refused") exception =
            "R" else if (exceptionSpinner!!.selectedItem.toString() == "Other") exception = "O"
        var pickup = "D"
        if (pickupbtn!!.currentTextColor == Color.BLACK) {
            pickup = "P"
        }
        val intent = Intent()
        intent.putExtra("sbolnum", sbolnum)
        intent.putExtra("picknum", picknum)
        intent.putExtra("signedby", signedby)
        intent.putExtra("pro", pro)
        intent.putExtra("bol", bol)
        intent.putExtra("pickup", pickup)
        intent.putExtra("cons", consignee)
        intent.putExtra("conscity", consigneecity)
        intent.putExtra("consstate", consigneestate)
        intent.putExtra("exception", exception)
        intent.putExtra("comments", comments)
        intent.putExtra("type", type)
        intent.putExtra("doccode", doccode)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {

                    val bol = data!!.getStringExtra("sbolnum")
                    val type = data!!.getIntExtra("type",0)

                    if(type ==2){
                        et_pro1!!.setText(bol)
                    }
                    else{
                        et_bol1!!.setText(bol)
                    }


                    val i = Intent()
                    i.putExtra("sbolnum", sbolnum)

                    i.putExtra("type", type)


                    //setResult(RESULT_OK, i)
                   // runOnUiThread { finish() }
                }
            }


    }
}