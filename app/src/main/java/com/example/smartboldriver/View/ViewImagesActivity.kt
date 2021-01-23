package com.example.smartboldriver.View

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.smartboldriver.R
import com.example.smartboldriver.utils.rotateBitmap
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.common.util.IOUtils
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class ViewImagesActivity : AppCompatActivity() {
    var pdfView: PDFView? = null
    var imgView: ImageView? = null
    var bolImg: ImageView? = null
    var pagenum_tv: TextView? = null
    var prevImg: Button? = null
    var nextImg: Button? = null
    var path: String? = null
    var images: ArrayList<String>? = ArrayList()
    var current = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images)
        pagenum_tv = findViewById(R.id.pagenum_tv) as TextView
        prevImg = findViewById(R.id.prevImg) as Button
        nextImg = findViewById(R.id.nextImg) as Button
        pdfView = findViewById(R.id.pdfView) as PDFView
        imgView = findViewById(R.id.imgView) as ImageView
        current = 0
        images = intent.getStringArrayListExtra("images")
//        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//        //      getSupportActionBar().setTitle(fileName.substring(fileName.lastIndexOf('/')+1));
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
       // toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })
        if (images!!.size == 0) {
            pdfView!!.visibility = View.INVISIBLE
            imgView!!.visibility = View.INVISIBLE
            nextImg!!.visibility = View.GONE
            prevImg!!.visibility = View.GONE
            pagenum_tv!!.visibility = View.VISIBLE
            pagenum_tv!!.text = "Nothing Available"
        } else {
            pdfView!!.visibility = View.VISIBLE
            imgView!!.visibility = View.VISIBLE
            nextImg!!.visibility = View.VISIBLE
            prevImg!!.visibility = View.VISIBLE
            pagenum_tv!!.visibility = View.VISIBLE
            path = images!![0]
            current = 0
            setView(current)
        }
        prevImg!!.setOnClickListener {
            current--
            setView(current)
        }
        nextImg!!.setOnClickListener {
            current++
            setView(current)
        }
    }

    fun setView(current: Int) {
        try {
            if (images != null) {
                if (current == 0) prevImg!!.visibility = View.INVISIBLE else prevImg!!.visibility =
                    View.VISIBLE
                if (current == images!!.size - 1) nextImg!!.visibility =
                    View.INVISIBLE else nextImg!!.visibility =
                    View.VISIBLE

                var count = current+1
                pagenum_tv!!.text = count.toString() + "/" + images!!.size
               // pagenum_tv!!.text=  current + 1+ "/" + images!!.size
               // pagenum_tv!!.text=   images!!.size.toString()
                path = images!![current]
                val imgstring = GetString(path)
                if (images!![current].contains(".jpg")) {
                    pdfView!!.visibility = View.GONE
                    imgView!!.visibility = View.VISIBLE
                    val bm =
                        decodeSampledBitmapFromUri(images!![current])
                    //val bitmap: Bitmap = SBUtils.rotateBitmap(path, bm)
                    val bitmap: Bitmap = rotateBitmap(path!!,bm!!)!!
                    imgView!!.setImageBitmap(bitmap)
                } else {
                    imgView!!.visibility = View.GONE
                    pdfView!!.visibility = View.VISIBLE
                    val bytes =
                        Base64.decode(imgstring, Base64.DEFAULT)
                    pdfView!!.fromBytes(bytes).defaultPage(0).spacing(60).load()
                }
            }
        } catch (e: Exception) {
        }
    }

    @Throws(IOException::class)
    private fun GetString(filepath: String?): String {
        val inputStream: InputStream = FileInputStream(filepath)
        val byteArray =
            IOUtils.toByteArray(inputStream)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeSampledBitmapFromUri(path: String?): Bitmap? {
        var bm: Bitmap? = null
        val options =
            BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inJustDecodeBounds = false
        bm = BitmapFactory.decodeFile(path, options)
        return bm
    }
}