package com.example.smartboldriver.features.shipments

import DeliveryAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.example.smartboldriver.R
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.UploadDataRequest
import com.example.smartboldriver.models.pickup.PickupResponseNew
import com.example.smartboldriver.utils.DBHelper
import com.example.smartboldriver.utils.getDeviceId
import com.example.smartboldriver.utils.getTimestamp
import com.example.smartboldriver.utils.getToken
import com.google.android.material.tabs.TabLayout
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Byte.decode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DeliveryListActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var pickupResponseNewonj : PickupResponseNew
    lateinit var mCurrentPhotoPath: String
    private var fileUri: Uri? = null
    var photoFile: File? = null
    var photoFileName = "photo.jpg"

    val db = DBHelper(this)
    companion object{
        val cameraRequest = 1888
    }

    var picUri: Uri? = null

    private val permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected: ArrayList<String> = ArrayList()
    private val permissions: ArrayList<String> = ArrayList()

    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_list)

        title = "Delivery List"
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Signed"))
        tabLayout.addTab(tabLayout.newTab().setText("Complete"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = DeliveryAdapter(
            this, supportFragmentManager,
            tabLayout.tabCount
        )
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun takePicture(item: PickupResponseNew) {
        pickupResponseNewonj = item

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, cameraRequest)

            dispatchTakePictureIntent()


    }



    private fun createImageFile(): File? {
      //  clearTempImages()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())



        photoFile = File(
            Environment
                .getExternalStorageDirectory().path + "/SmartBOL/Completed BOL/",
            "IMG_" + timeStamp +
                    ".jpg"
        )

        fileUri = Uri.fromFile(photoFile)


        mCurrentPhotoPath = photoFile!!.absolutePath
        return photoFile
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }


               // var compressedImageFile = Compressor(this).compressToFile(photoFile)
                //val file_size = java.lang.String.valueOf(photoFile!!.length() / 1024).toInt()
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.scanlibrary.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, cameraRequest)
                }
            }
        }
    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {


        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    @Throws(IOException::class)
    private fun getBitmap(selectedimg: Uri): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 3
        var fileDescriptor: AssetFileDescriptor? = null
        fileDescriptor =
            this.getContentResolver().openAssetFileDescriptor(selectedimg, "r")
        return BitmapFactory.decodeFileDescriptor(
            fileDescriptor!!.fileDescriptor, null, options
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        if (resultCode == Activity.RESULT_OK){

          //  getImageSizeFromUriInMegaByte()
//            val result = CropImage.getActivityResult(data)
//            val imageUri = result.uri
//            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
           // var comments = getComntDialog()
            when(requestCode){
                cameraRequest -> {
                    //store this in db

                    var compressedImageFile = Compressor(this).compressToFile(photoFile)
                    mCurrentPhotoPath = compressedImageFile.absolutePath

                    val input = EditText(this)
                    var coment: String = ""
                    val alertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setMessage("Comment")
                    alertDialog.setView(input, 10, 0, 10, 0) // 10 spacing, left and right

                    alertDialog.setButton("OK", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            coment = input.text.toString()

                            val sbolnum: String = pickupResponseNewonj.sbolnum!!

                            val picknum: String = pickupResponseNewonj.picknum!!
                            val pickup: String = pickupResponseNewonj.pickup!!
                            val comments: String = "Test"
                            val type: String = "img"
                            val status: String = "n"
                            val doccode: String = pickupResponseNewonj.doccode!!

                            val gps: String = ""
                            //  val gps: String = getLatLong(this@MainMenu)
                            val timestamp: String = getTimestamp()!!
                            val mydb = DBHelper(this@DeliveryListActivity)


                            val queueObject = QueueObject(
                                "img",
                                timestamp,
                                "",
                                mCurrentPhotoPath,
                                "n",
                                "",
                                "",
                                "",
                                coment,
                                sbolnum!!,
                                "",
                                gps,
                                "",
                                "",
                                "",
                                "",
                                doccode,
                                "",
                                picknum,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                pickup
                            )
                            db.addImage(queueObject)

                            val request = UploadDataRequest(
                                getDeviceId(),
                                getToken(),
                                "udimg",
                                "DP",
                                "Delivery Photo",
                                "jpg",
                                "filename",
                                pickupResponseNewonj.picknum!!,
                                pickupResponseNewonj.sbolnum,
                                "",
                                pickupResponseNewonj.shipnum,
                                pickupResponseNewonj.bolnum,
                                "",
                                coment,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                pickup,
                                "D",
                                mCurrentPhotoPath,
                                "P",
                                timestamp
                            )
                        }
                    })
                    alertDialog.show()

                    Log.d("Bitmap", data.toString())

////...........
//                    val sbolnum: String = pickupResponseNewonj.sbolnum!!
//
//                    val picknum: String = pickupResponseNewonj.picknum!!
//                    val pickup: String = pickupResponseNewonj.pickup!!
//                    val comments: String = "Test"
//                    val type: String = "img"
//                    val status: String = "n"
//                    val doccode: String = pickupResponseNewonj.doccode!!
//
//                    val gps: String = ""
//                    //  val gps: String = getLatLong(this@MainMenu)
//                    val timestamp: String = getTimestamp()!!
//                    val mydb = DBHelper(this)
//
//
//                    val queueObject = QueueObject(
//                        "img",
//                        timestamp,
//                        "",
//                        mCurrentPhotoPath,
//                        "n",
//                        "",
//                        "",
//                        "",
//                        comments,
//                        sbolnum!!,
//                        "",
//                        gps,
//                        "",
//                        "",
//                        "",
//                        "",
//                        doccode,
//                        "",
//                        picknum,
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        pickup
//                    )
//                    db.addImage(queueObject)
//
//                    val request = UploadDataRequest(
//                        getDeviceId(),
//                        getToken(),
//                        "udimg",
//                        "DP",
//                        "Delivery Photo",
//                        "jpg",
//                        "filename",
//                        pickupResponseNewonj.picknum!!,
//                        pickupResponseNewonj.sbolnum,
//                        "",
//                        pickupResponseNewonj.shipnum,
//                        pickupResponseNewonj.bolnum,
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        pickup,
//                        "D",
//                        mCurrentPhotoPath,
//                        "P",
//                        timestamp
//                    )
                    // uploadFile(request, mCurrentPhotoPath, pickupResponseNewonj.doccode!!)
                    val file_name =
                        mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1)
                    //  db.insertCaptureImage(byteArray)

                    //   }


                }
            }
        }

}

    private fun getComntDialog(): String {
        val input = EditText(this)
        var coment:String=""

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("Message")
        alertDialog.setView(input, 10, 0, 10, 0) // 10 spacing, left and right

        alertDialog.setButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                coment = input.text.toString()
            }
        })
        alertDialog.show()
       return coment;

    }
}