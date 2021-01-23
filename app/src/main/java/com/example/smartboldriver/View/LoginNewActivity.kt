package com.example.smartboldriver.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smartboldriver.R
import com.example.smartboldriver.features.dashboard.DashBoardActivity

class LoginNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)
        val btn_login = findViewById<TextView>(R.id.login_txt)
        btn_login.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent);
        }
    }
}