package com.example.smartboldriver.View

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.smartboldriver.R
import com.example.smartboldriver.utils.DBHelper
import com.example.smartboldriver.utils.rotateBitmap
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.common.util.IOUtils
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class ActiveBOLActivity : AppCompatActivity() {
    var pdfView: PDFView? = null
    var imgView: ImageView? = null
    var pagenum_tv: TextView? = null
    var previous: Button? = null
    var next: Button? = null
    var textview: TextView? = null
    var pdfpath: String? = null
    var imgstring: String? = null
    var currentpdf = 0
    var picknums: ArrayList<String>? = null
    var sbolnums: ArrayList<String>? = null
    var bolnums: ArrayList<String>? = null
    var pdfs: ArrayList<String>? = null
    var mydb = DBHelper(this)
    var which = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_b_o_l)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        picknums = intent.getStringArrayListExtra("multipick")
        sbolnums = intent.getStringArrayListExtra("multisbol")
        bolnums = intent.getStringArrayListExtra("multibol")
        pdfs = intent.getStringArrayListExtra("multipdfs")
        previous = findViewById(R.id.previous) as Button
        next = findViewById(R.id.next) as Button
        textview = findViewById(R.id.textview) as TextView
        pagenum_tv = findViewById(R.id.pagenum_tv) as TextView
        pdfView = findViewById(R.id.pdfView) as PDFView
        imgView = findViewById(R.id.imgView) as ImageView
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        toolbar.setNavigationOnClickListener { finish() }
        if (pdfs!!.size == 0) //if no active bols show blank screen
        {
            pdfView!!.visibility = View.INVISIBLE
            imgView!!.visibility = View.INVISIBLE
            next!!.visibility = View.INVISIBLE
            previous!!.visibility = View.INVISIBLE
            textview!!.visibility = View.VISIBLE
        } else {
            next!!.visibility = View.VISIBLE
            previous!!.visibility = View.VISIBLE
            textview!!.visibility = View.INVISIBLE
            pdfpath = pdfs!![0]
            currentpdf = 0
            supportActionBar!!.setTitle(bolnums!![currentpdf])
            setView(currentpdf)
        }
        previous!!.setOnClickListener {
            currentpdf--
            supportActionBar!!.setTitle(bolnums!![currentpdf])
            setView(currentpdf)
        }
        next!!.setOnClickListener {
            currentpdf++
            supportActionBar!!.setTitle(bolnums!![currentpdf])
            setView(currentpdf)
        }
    }

    override fun getSupportActionBar(): ActionBar? {
        return super.getSupportActionBar()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//        when (item.itemId) {
//          //  android.R.id.home -> Toast.makeText(application, "Back", Toast.LENGTH_LONG).show()
//            else -> {
//            }
//        }
//    }
override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
}

    fun setView(currentpdf: Int) {
        try {
            if (pdfs != null) {
                if (currentpdf == 0) previous!!.visibility =
                    View.INVISIBLE else previous!!.visibility =
                    View.VISIBLE
                if (currentpdf == pdfs!!.size - 1) next!!.visibility =
                    View.INVISIBLE else next!!.visibility =
                    View.VISIBLE

                var count = currentpdf+1
                pagenum_tv!!.text = count.toString() + "/" + pdfs!!.size
               // pagenum_tv!!.setText(currentpdf + 1 /*+ "/" + pdfs!!.size*/)
                pdfpath = pdfs!![currentpdf]
                imgstring = GetString(pdfpath)
                if (pdfpath!!.contains(".jpg")) { //If image then display imgview
                    pdfView!!.visibility = View.GONE
                    imgView!!.visibility = View.VISIBLE
                    val bm = decodeSampledBitmapFromUri(pdfpath)
                    val bitmap: Bitmap = rotateBitmap(pdfpath!!, bm!!)!!
                    imgView!!.setImageBitmap(bitmap)
                } else { //this shows pdf view
                    imgView!!.visibility = View.GONE
                    pdfView!!.visibility = View.VISIBLE
                    val bytes = Base64.decode(imgstring, Base64.DEFAULT)
                    pdfView!!.fromBytes(bytes).defaultPage(0).spacing(60).load()
                }
            }
        } catch (e: Exception) {
        }
    }

    @Throws(IOException::class)
    private fun GetString(filepath: String?): String? {
        val inputStream: InputStream = FileInputStream(filepath)
        val byteArray = IOUtils.toByteArray(inputStream)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeSampledBitmapFromUri(path: String?): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inJustDecodeBounds = false
        bm = BitmapFactory.decodeFile(path, options)
        return bm
    }

    override fun onBackPressed() {
        super.onBackPressed()

      //  finish()
    }


}