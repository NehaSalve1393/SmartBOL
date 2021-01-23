package com.example.smartboldriver.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smartboldriver.R

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val details_next = findViewById<TextView>(R.id.details_next)

        details_next.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, CarrierDetailsActivity::class.java)
            startActivity(intent);
        }
    }
}