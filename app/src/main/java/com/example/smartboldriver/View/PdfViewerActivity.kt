package com.example.smartboldriver.View

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.smartboldriver.R
import com.example.smartboldriver.View.fragments.dummy.ImsgesFragment
import com.example.smartboldriver.features.shipments.DelSignActivity
import com.example.smartboldriver.features.shipments.DeliveryListActivity
import com.example.smartboldriver.utils.DBHelper
import com.example.smartboldriver.utils.localLogout
import com.example.smartboldriver.utils.rotateBitmap
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.common.util.IOUtils
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class PdfViewerActivity : AppCompatActivity() {
    var pdfView: PDFView? = null
    var imgView: ImageView? = null
    var pagenum_tv: TextView? = null
    var capturerecpod: Button? = null
    var previous: Button? = null
    var next: Button? = null
    var sign: Button? = null
    var pdfpath: String? = null
    var imgstring: String? = null
    var pickup: String? = null
    var status: String? = null
    var currentpdf = 0
    var picknums: ArrayList<String>? = null
    var sbolnums: ArrayList<String>? = null
    var bolnums: ArrayList<String>? = null
    var pdfs: ArrayList<String>? = null
    var mydb: DBHelper = DBHelper(this)
    private val SWIPE_MIN_DISTANCE = 120
    private val SWIPE_MAX_OFF_PATH = 250
    private val SWIPE_THRESHOLD_VELOCITY = 200
    private val gestureDetector: GestureDetector? = null
    var gestureListener: View.OnTouchListener? = null


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.pdf_menubar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.viewImages -> {
//                val images: ArrayList<String> =
//                    mydb.getImages(sbolnums!![currentpdf])!!
//                if (images.size == 0) {
//                    Toast.makeText(
//                        this,
//                        "No Images Taken",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    val intent1 = Intent(
//                        this,
//                        ViewImagesActivity::class.java
//                    )
//                    intent1.putExtra("images", images)
//                    startActivity(intent1)
//
//
//
//                }
//                true
//
//            }
            R.id.previousVersions -> {
                val data: HashMap<*, *> =
                    mydb.getPreviousVersions(sbolnums!![currentpdf])!!
                picknums = data["picks"] as ArrayList<String>?
                sbolnums = data["sbols"] as ArrayList<String>?
                bolnums = data["bols"] as ArrayList<String>?
                pdfs = data["pdfs"] as ArrayList<String>?
                val intent2 = Intent(
                    this,
                    ViewImagesActivity::class.java
                )
                intent2.putStringArrayListExtra("multipick", picknums)
                intent2.putStringArrayListExtra("multisbol", sbolnums)
                intent2.putStringArrayListExtra("multibol", bolnums)
                intent2.putStringArrayListExtra("images", pdfs)
                startActivity(intent2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        picknums = intent.getStringArrayListExtra("multipick")
        sbolnums = intent.getStringArrayListExtra("multisbol")
        bolnums = intent.getStringArrayListExtra("multibol")
        pdfs = intent.getStringArrayListExtra("multipdfs")
        pickup = intent.getStringExtra("pickup")
        status = intent.getStringExtra("status")
        pdfpath = pdfs!![0]
        currentpdf = 0
        capturerecpod = findViewById(R.id.capturerecpod) as Button
        previous = findViewById(R.id.previous) as Button
        next = findViewById(R.id.next) as Button
        sign = findViewById(R.id.sign) as Button
        pagenum_tv = findViewById(R.id.pagenum_tv) as TextView
//        val data: HashMap<*, *> =
//        mydb.getPreviousVersions(sbolnums!![currentpdf])!!
//        var pdfs1 = data["pdfs"] as ArrayList<String>?
//
//        if(pdfs1!=null){
//            if(pdfs1!!.size>0){
//                pdfs!!.addAll(pdfs1!!)
//            }
//        }


        setView(currentpdf)
        capturerecpod!!.setOnClickListener { /* picknums = getIntent().getStringArrayListExtra("multipick");
                        sbolnums = getIntent().getStringArrayListExtra("multisbol");
                        bolnums = getIntent().getStringArrayListExtra("multibol");
                        pdfs = getIntent().getStringArrayListExtra("multipdfs");
                        String sbol = sbolnums.get(currentpdf);
                        String picknum = picknums.get(currentpdf);

                        Intent intent = new Intent(PdfViewer.this, TakePic.class);
                        intent.putExtra("sbol",sbol);
                        intent.putExtra("picknum", picknum);
                        intent.putExtra("type", "pod");
                        intent.putExtra("doccode", "rec");
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
            val intent = Intent(
                this,
                PODBlankPageActivity::class.java
            )
            intent.putExtra("sbol", sbolnums!![currentpdf])
            intent.putExtra("bol", bolnums!![currentpdf])
            intent.putExtra("picknum", picknums!![currentpdf])
            intent.putExtra("pickup", pickup)
            intent.putExtra("type", "pod")
            intent.putExtra("doccode", "rec")
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
        }
        sign!!.setOnClickListener {
            if(status.equals("y") || status.equals("yyy")) {
                //toast



                val builder = AlertDialog.Builder(this)
                builder.setTitle("Warning!")
                builder.setMessage("This delivery has been signed. Do you want to resign ?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    var sbolnum = ""
                    for (sbol in sbolnums!!) {

                    }
                    picknums = intent.getStringArrayListExtra("multipick")
                    sbolnums = intent.getStringArrayListExtra("multisbol")
                    pdfs = intent.getStringArrayListExtra("multipdfs")
                    val intent = Intent(
                        applicationContext,
                        DelSignActivity::class.java
                    )
                    intent.putStringArrayListExtra("picknums", picknums)
                    intent.putStringArrayListExtra("sbolnums", sbolnums)
                    intent.putExtra("pickup", pickup)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(intent)

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }

            else{


                var sbolnum = ""
                for (sbol in sbolnums!!) {

                }
                picknums = intent.getStringArrayListExtra("multipick")
                sbolnums = intent.getStringArrayListExtra("multisbol")
                pdfs = intent.getStringArrayListExtra("multipdfs")
                val intent = Intent(
                    applicationContext,
                    DelSignActivity::class.java
                )
                intent.putStringArrayListExtra("picknums", picknums)
                intent.putStringArrayListExtra("sbolnums", sbolnums)
                intent.putExtra("pickup", pickup)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)

            }

        }
        previous!!.setOnClickListener {
            currentpdf--
            setView(currentpdf)
        }
        next!!.setOnClickListener {
            currentpdf++
            setView(currentpdf)
        }
    }

    fun setView(currentpdf: Int) {
        try {
        //    val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
//            setSupportActionBar(toolbar)
//            supportActionBar!!.setTitle(bolnums!![currentpdf])
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            supportActionBar!!.setDisplayShowHomeEnabled(true)
//            toolbar.setNavigationOnClickListener(View.OnClickListener {
//                picknums = null
//                sbolnums = null
//                bolnums = null
//                pdfpath = null
//                //                    Intent intent = new Intent();
//                //                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION |
//                //                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //                    startActivity(intent);
//                //                    overridePendingTransition(0, 0);
//                finish()
//            })
            pdfView = findViewById(R.id.pdfView) as PDFView
            imgView = findViewById(R.id.imgView) as ImageView
            if (pdfs != null) {
                if (currentpdf == 0) previous!!.visibility =
                    View.INVISIBLE else previous!!.visibility =
                    View.VISIBLE
                if (currentpdf == pdfs!!.size - 1) next!!.visibility =
                    View.INVISIBLE else next!!.visibility =
                    View.VISIBLE

               // tvCount.text = count.toString() +" /"+bitmapArr.size

                var count = currentpdf+1
                pagenum_tv!!.text = count.toString() + "/" + pdfs!!.size
               // pagenum_tv.text(currentpdf + 1 , "/" + pdfs!!.size)
              //  pagenum_tv.text(currentpdf + 1 )
               // pagenum_tv!!.setText(currentpdf+1)!!


                // int imgcount = mydb.getNumImages(sbolnum.get(currentpdf));
                // takepic.setText("Take Picture (" + imgcount + ")");
                pdfpath = pdfs!![currentpdf]
               // imgstring = GetString(pdfpath)
                if (pdfpath!!.contains(".jpg")) {
                    pdfView!!.visibility = View.GONE
                    imgView!!.visibility = View.VISIBLE
                    sign!!.visibility = View.INVISIBLE
                    val bm = decodeSampledBitmapFromUri(pdfpath)
                    val bitmap: Bitmap = rotateBitmap(pdfpath!!, bm!!)!!
                    imgView!!.setImageBitmap(bitmap)
                } else {
                    imgView!!.visibility = View.GONE
                    pdfView!!.visibility = View.VISIBLE
                    sign!!.visibility = View.VISIBLE
                    val bytes =
                        Base64.decode(pdfpath, Base64.DEFAULT)

                    pdfView!!.fromBytes(bytes).defaultPage(0).spacing(60).load()
                }
            }
        } catch (e: Exception) {
        }
    }

    @Throws(IOException::class)
    private fun GetString(filepath: String?): String? {
        val inputStream: InputStream = FileInputStream(filepath)
        val byteArray =
            IOUtils.toByteArray(inputStream)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        currentpdf = 0
        picknums = null
        sbolnums = null
        bolnums = null
        pdfpath = null
        if (pickup == "D") {
            val intent = Intent(
                applicationContext,
                DeliveryListActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else if (pickup == "P") {
            val intent = Intent(
                applicationContext,
                DeliveryListActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        finish()
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