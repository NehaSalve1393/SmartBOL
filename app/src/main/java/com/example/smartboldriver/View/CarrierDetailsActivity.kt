package com.example.smartboldriver.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smartboldriver.R

class CarrierDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrier_details)
        val carrier_next_btn_txt  = findViewById<TextView>(R.id.carrier_next_btn_txt)
        carrier_next_btn_txt.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, ShipperInfoActivity::class.java)
            startActivity(intent);
        }
    }
}