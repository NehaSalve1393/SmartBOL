package com.example.smartboldriver.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smartboldriver.R

class ConsigneeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consignee_info)
        val next_txt_btn  = findViewById<TextView>(R.id.next_txt_btn)
        next_txt_btn.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, LineItemsActivity::class.java)
            startActivity(intent);
        }
    }
}