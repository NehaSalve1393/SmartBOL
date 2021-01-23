package com.example.smartboldriver.View

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.utils.getDeviceId
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.common.util.IOUtils
import kotlinx.android.synthetic.main.activity_bol_list.*
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class BolListActivity : AppCompatActivity() {

    var picknum: String? = null
    var location: String? = null
    var imgstring: String? = null
    var pdfView: PDFView? = null
    var pdfpath: String? = null
    var pdfpath1: String? = null
    var stype: Int? = 0
    var pdfs: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        picknum = intent.getStringExtra("scanCode")
        location = intent.getStringExtra("scanLocation")
        pdfpath1 = intent.getStringExtra("path")
        pdfs = intent.getStringArrayListExtra("multipdfs")
        stype = intent.getIntExtra("screenType", 0)


        var cons = intent.getStringExtra("consinee")
        var city = intent.getStringExtra("city")
        var unit = intent.getStringExtra("unit")
        var weight = intent.getStringExtra("weight")

        if(pdfs!==null && pdfs!!.size>0)
        pdfpath = pdfs!![0]




        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = sdf.format(Date())

        setContentView(R.layout.activity_bol_list)
        if(stype ==10){
            tv_Info.setText(cons+", "+city+": "+unit+" ,"+weight)
        }


        pdfView = findViewById(R.id.pdfView1) as PDFView

        if( stype !=null&& stype == 1){
            btn_done.visibility = View.VISIBLE
            btn_sign1.visibility = View.GONE
            btn_shipment_count.visibility = View.GONE
        }

 var pdfpath2 = "https://cltest.smartbol.com/api3/a102.aspx?k="+"NgkPgVhxuBonHh-77_FwZ6y5TK4myJ"+"&d="+getDeviceId()+"&k2="+System.currentTimeMillis().toString()
//        Log.e("PDF PATH---", pdfpath2)
//        var url2 = "https://cltest.smartbol.com/api3/a102.aspx?k=joFjNFQPnUy9CoDyzQmnhfoEdtipDn&d=PYXH7TIWZ&k2=1605460578.9018068"
//        Log.e("PDF PATH-333--", url2)
//        val url = URL(url2)
//        var uri = Uri.parse(url2)
//
//        //new
//
//
//        val input = URL(pdfpath2).openStream()
//        pdfView!!.fromUri(uri!!).load()
//        //

        if(stype ==10) {
//            val bytes = Base64.decode(pdfpath, Base64.DEFAULT)
//            pdfView!!.fromBytes(bytes).defaultPage(0).spacing(60).load()

            object : AsyncTask<Void?, Void?, Void?>() {

                protected override fun doInBackground(vararg voids: Void?): Void? {
                    try {
                        val input = URL(pdfpath2).openStream()
                        pdfView!!.fromStream(input).load()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return null
                }
            }.execute()
        }
        else{
            val bytes = Base64.decode(pdfpath, Base64.DEFAULT)
            pdfView!!.fromBytes(bytes).defaultPage(0).spacing(60).load()
        }

        btn_done.setOnClickListener { finish() }
        btn_sign1.setOnClickListener{


            val intent: Intent = Intent(
                this,
                SignActivity::class.java
            )

            intent.putExtra("scanCode", picknum)
            intent.putExtra("scanLocation", location)



            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    @Throws(IOException::class)
    private fun GetString(filepath: String?): String? {
        val inputStream: InputStream = FileInputStream(filepath)
        val byteArray = IOUtils.toByteArray(inputStream)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}