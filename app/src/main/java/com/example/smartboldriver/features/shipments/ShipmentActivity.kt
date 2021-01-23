package com.example.smartboldriver.features.shipments

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.example.smartboldriver.R
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.UploadDataRequest
import com.example.smartboldriver.models.pickup.PickupResponseNew
import com.example.smartboldriver.utils.*
import com.google.android.material.tabs.TabLayout
import com.scanlibrary.ScanConstants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShipmentActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_shipment)

        title = "Delivery List"
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Signed"))
        tabLayout.addTab(tabLayout.newTab().setText("Complete"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = ShipmentAdapter(
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

//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val file = createImageFile()
//        val isDirectoryCreated = file!!.parentFile.mkdirs()
//        Log.d("", "openCamera: isDirectoryCreated: $isDirectoryCreated")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            val tempFileUri = FileProvider.getUriForFile(
//                this.getApplicationContext(),
//                "com.scanlibrary.provider",  // As defined in Manifest
//                file
//            )
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
//        } else {
//            val tempFileUri = Uri.fromFile(file)
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
//        }
//        startActivityForResult(cameraIntent, ScanConstants.START_CAMERA_REQUEST_CODE)

    }


//    @Throws(IOException::class)
//    private fun createImageFile(): File? {
//
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",  /* suffix */
//            storageDir /* directory */
//        )
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.absolutePath
//        return image
//    }

    private fun createImageFile(): File? {
        //  clearTempImages()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val file = File(
            ScanConstants.IMAGE_PATH, "IMG_" + timeStamp +
                    ".jpg"
        )
        fileUri = Uri.fromFile(file)
        mCurrentPhotoPath = file.absolutePath
        return file
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
            when(requestCode){
                cameraRequest -> {
                    //store this in db


//                    val uri = data!!.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
//                    val bitmap = MediaStore.Images.Media.getBitmap(
//                        contentResolver, uri
//                    )
//                    contentResolver.delete(uri!!, null, null)
//                    val byteArrayOutputStream = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//                    val byteArray = byteArrayOutputStream.toByteArray()
//                    val imgstr = Base64.encodeToString(byteArray, Base64.DEFAULT)




                    Log.d("Bitmap",data.toString())

//...........
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
                    val mydb = DBHelper(this)


                    val queueObject = QueueObject(
                        "img",
                        timestamp,
                        "",
                        mCurrentPhotoPath,
                        "n",
                        "",
                        "",
                        "",
                        comments,
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
                        "",
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
                    uploadFile(
                        request,
                        mCurrentPhotoPath,
                        pickupResponseNewonj.doccode!!,
                        applicationContext
                    )
                    val file_name =
                        mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1)
                    //  db.insertCaptureImage(byteArray)

                }
            }
        }

    }
}