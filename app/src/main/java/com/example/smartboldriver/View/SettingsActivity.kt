package com.example.smartboldriver.View

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.SmartBOLDriver
import com.example.smartboldriver.features.authentication.LoginActivity
import com.example.smartboldriver.utils.*
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {
    val mydb = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings)
        device_id_value.text = getDeviceId()+" "+ getUserId()
        account_value.text = getAccount()


        var sharedpreferences = SmartBOLDriver.applicationContext().getSharedPreferences(
            "SharedPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val testflag: String = sharedpreferences!!.getString("Test", "n")!!
        if (testflag == "y") {
            switch1.setChecked(true);
            //SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/

        }
        else{
            switch1.setChecked(false);
           // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx/"
        }

        var delete: String? = ""
        if (sharedpreferences.contains("Delete")) {
            delete = sharedpreferences.getString("Delete", "")
            tm_frame_edt_txt.setText(delete)
        }


        switch1.setOnClickListener {

            if (switch1.isChecked) {
                // The switch is enabled/checked

                var alert = ""
                val testflag: String = sharedpreferences!!.getString("Test", "n")!!
                if (sharedpreferences!!.contains("Test")) {
                    alert = if (testflag == "y") {
                        "Changing from test environment to production environment will erase all app data"
                    } else {
                        "Changing from production environment to test environment will erase all app data"
                    }
                }
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        //dialog.setCancelable(false);
                        val editor: SharedPreferences.Editor =
                            sharedpreferences!!.edit()
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                if (testflag == "y") {
                                    editor.putString("Test", "n")
                                    //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
                                    // testlabel.setVisibility(View.GONE)
                                } else {
                                    editor.putString("Test", "y")
                                    // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
                                    //testlabel.setVisibility(View.VISIBLE)
                                }
                                editor.commit()
                                val db = DBHelper(this)
                                mydb.deleteRecordsFromDb()
                                db.getWritableDatabase().execSQL("delete from gps")
                                db.getWritableDatabase().execSQL("delete from img")
                                db.getWritableDatabase().execSQL("delete from shipments")


                                //
                                val intent = Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                //
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                dialog.dismiss()
                                dialog.cancel()
                                switch1.setChecked(false);

                            }
                        }
                    }

                val builder: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this)
                builder.setTitle("WARNING")
                    .setMessage(alert).setPositiveButton("Continue", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show()

                // Change the app background color

            } else {
                // The switch is disabled

                var alert = ""
                val testflag: String = sharedpreferences!!.getString("Test", "n")!!
                if (sharedpreferences!!.contains("Test")) {
                    alert = if (testflag == "y") {
                        "Changing from test environment to production environment will erase all app data"
                    } else {
                        "Changing from production environment to test environment will erase all app data"
                    }
                }
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        val editor: SharedPreferences.Editor =
                            sharedpreferences!!.edit()
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                if (testflag == "y") {
                                    editor.putString("Test", "n")
                                    //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
                                    // testlabel.setVisibility(View.GONE)
                                } else {
                                    editor.putString("Test", "y")
                                    // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
                                    //testlabel.setVisibility(View.VISIBLE)
                                }
                                editor.commit()
                                val db = DBHelper(this)
                                mydb.deleteRecordsFromDb()
                                db.getWritableDatabase().execSQL("delete from gps")
                                db.getWritableDatabase().execSQL("delete from img")
                                db.getWritableDatabase().execSQL("delete from shipments")

                                val intent = Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                switch1.setChecked(true);
                                dialog.cancel()
                                dialog.dismiss()

                            }
                        }
                    }
                val builder: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this)
                builder.setTitle("WARNING")
                    .setMessage(alert).setPositiveButton("Continue", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show()
                // Set the app background color to light gray

            }

        }
//        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                // The switch is enabled/checked
//
//                var alert = ""
//                val testflag: String = sharedpreferences!!.getString("Test", "n")!!
//                if (sharedpreferences!!.contains("Test")) {
//                    alert = if (testflag == "y") {
//                        "Changing from test environment to production environment will erase all app data"
//                    } else {
//                        "Changing from production environment to test environment will erase all app data"
//                    }
//                }
//                val dialogClickListener =
//                    DialogInterface.OnClickListener { dialog, which ->
//                        val editor: SharedPreferences.Editor =
//                            sharedpreferences!!.edit()
//                        when (which) {
//                            DialogInterface.BUTTON_POSITIVE -> {
//                                if (testflag == "y") {
//                                    editor.putString("Test", "n")
//                                    //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
//                                   // testlabel.setVisibility(View.GONE)
//                                } else {
//                                    editor.putString("Test", "y")
//                                    // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
//                                    //testlabel.setVisibility(View.VISIBLE)
//                                }
//                                editor.commit()
//                                val db = DBHelper(this)
//                                mydb.deleteRecordsFromDb()
//                                db.getWritableDatabase().execSQL("delete from gps")
//                                db.getWritableDatabase().execSQL("delete from img")
//                                db.getWritableDatabase().execSQL("delete from shipments")
//
//
//                                //
//                                val intent = Intent(
//                                    this,
//                                    LoginActivity::class.java
//                                )
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)
//                                //
//                            }
//                            DialogInterface.BUTTON_NEGATIVE -> {
//                                dialog.dismiss()
//                                dialog.cancel()
//                                switch1.setChecked(false);
//
//                            }
//                        }
//                    }
//                val builder: android.app.AlertDialog.Builder =
//                    android.app.AlertDialog.Builder(this)
//                builder.setTitle("WARNING")
//                    .setMessage(alert).setPositiveButton("Continue", dialogClickListener)
//                    .setNegativeButton("Cancel", dialogClickListener).show()
//
//                // Change the app background color
//
//            } else {
//                // The switch is disabled
//
//                var alert = ""
//                val testflag: String = sharedpreferences!!.getString("Test", "n")!!
//                if (sharedpreferences!!.contains("Test")) {
//                    alert = if (testflag == "y") {
//                        "Changing from test environment to production environment will erase all app data"
//                    } else {
//                        "Changing from production environment to test environment will erase all app data"
//                    }
//                }
//                val dialogClickListener =
//                    DialogInterface.OnClickListener { dialog, which ->
//                        val editor: SharedPreferences.Editor =
//                            sharedpreferences!!.edit()
//                        when (which) {
//                            DialogInterface.BUTTON_POSITIVE -> {
//                                if (testflag == "y") {
//                                    editor.putString("Test", "n")
//                                    //   SmartBOL24Retrofit.baseUrl = "https://cltest.smartbol.com/api3/"
//                                   // testlabel.setVisibility(View.GONE)
//                                } else {
//                                    editor.putString("Test", "y")
//                                    // SmartBOL24Retrofit.baseUrl = "https://api.smartbol.com/api/txm.aspx"
//                                    //testlabel.setVisibility(View.VISIBLE)
//                                }
//                                editor.commit()
//                                val db = DBHelper(this)
//                                mydb.deleteRecordsFromDb()
//                                db.getWritableDatabase().execSQL("delete from gps")
//                                db.getWritableDatabase().execSQL("delete from img")
//                                db.getWritableDatabase().execSQL("delete from shipments")
//                            }
//                            DialogInterface.BUTTON_NEGATIVE -> {
//                                switch1.setChecked(true);
//                                dialog.cancel()
//                               dialog.dismiss()
//
//                            }
//                        }
//                    }
//                val builder: android.app.AlertDialog.Builder =
//                    android.app.AlertDialog.Builder(this)
//                builder.setTitle("WARNING")
//                    .setMessage(alert).setPositiveButton("Continue", dialogClickListener)
//                    .setNegativeButton("Cancel", dialogClickListener).show()
//                // Set the app background color to light gray
//
//            }
//        }
        txt_logout.setOnClickListener {
           // localLogout(this)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Warning - You are going to log out . Logging out will erase all app data. Are you sure you want to continue")
            builder.setPositiveButton("Continue") { dialog, which ->
                localLogout(this)
                mydb.deleteRecordsFromDb()

                //Toast.makeText(applicationContext, "continuar", Toast.LENGTH_SHORT).show()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val sharedpreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)
        val editor = sharedpreferences.edit()


        editor.putString(
            "Delete",
            tm_frame_edt_txt.getText().toString()
        )

        editor.commit()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}