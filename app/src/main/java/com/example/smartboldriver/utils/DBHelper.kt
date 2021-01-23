package com.example.smartboldriver.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.smartboldriver.models.QueueObject
import com.example.smartboldriver.models.pickup.PickupResponseNew
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DBHelper(context: Context?): SQLiteOpenHelper(context, "MyDBName.db", null, 1) {
    val DATABASE_NAME = "MyDBName.db"



    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
        db.execSQL(
            "create table gps " +
                    "(id integer primary key autoincrement, timestamp text, latitude text, longitude text, trailer text, status text, returntime text)"
        )
        db.execSQL(
            "create table images " +
                    "(id integer primary key autoincrement, type text, timestamp text, uploadtimestamp text, path BLOB, status text," +
                    "shipment text, trailer text, bol text, comment text," +
                    "sbolnum text, signedby text, gps text, signhash text, signinfo text, signimage text, token text, doccode text, email text, picknum text, exception text, account text, pro text, cons text,conscity text,consstate text, pickup text)"
        )
        db.execSQL(
            "create table shipments " +
                    "(id integer primary key autoincrement, sbolnum text, txdate text, shipdate text, srclocation text, doccode text, picknum text, delvnum text, bolnum text, shipnum text," +
                    "shipper text, shipadd1 text, shipadd2 text, shipcity text, shipstate text, shipzip text, shipcntry text, shipacc text, " +
                    "consignee text, consadd1 text, consadd2 text, conscity text, consstate text, conszip text, conscntry text, consattn text, consacc text, " +
                    "hazmat text, hmcontact text, spinstr text, stopnum text, status text, downloaddate text, token text, path text, pronum text, comments text, pickup text)"
        )

//        db.execSQL(
//            "create table img "+
//                    "(id integer primary key autoincrement, imagedata BLOB)"
//        )
        db.execSQL(
            "create table img " +
                    "(id integer primary key autoincrement, type text, timestamp text, uploadtimestamp text, path text, status text, " +
                    " shipment text, trailer text, bol text, comment text," +
                    "sbolnum text, signedby text, gps text, signhash text, signinfo text, signimage text, token text, doccode text, email text, picknum text, exception text, account text, pro text, cons text,conscity text,consstate text, pickup text)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS gps");
//        db.execSQL("DROP TABLE IF EXISTS images");
//        db.execSQL("DROP TABLE IF EXISTS shipments");
        onCreate(db)
    }
    fun insertCaptureImage1(bytes: ByteArray?) {
        val cv = ContentValues()
        cv.put("imagedata", bytes)
        Log.e("inserted", "inserted")
        writableDatabase.insert("img", null, cv)
    }

//    fun getAllCaptureImages(): ArrayList<Bitmap> {
//        val c: Cursor = readableDatabase.rawQuery("SELECT imagedata FROM img", null)
//        val bitmapList = arrayListOf<Bitmap>()
//        if (c.count > 0) {
//            while (c.moveToNext()) {
//                val bytes = c.getBlob(c.getColumnIndex("imagedata"))
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                bitmapList.add(bitmap)
//            }
//            Log.e("total images size", "" + bitmapList.toString())
//        }
//        c.close()
//        return bitmapList
//    }

    fun insertCaptureImage(queueObject: QueueObject): Boolean {
      /*  val cv = ContentValues()
        cv.put("imagedata", bytes)
        Log.e("inserted", "inserted")
        writableDatabase.insert("img", null, cv)*/

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sbolnum", queueObject.sbolnum)
        contentValues.put("type", queueObject.type)
        contentValues.put("timestamp", queueObject.timestamp)
        contentValues.put("shipment", queueObject.shipment)
        contentValues.put("trailer", queueObject.trailer)
        contentValues.put("bol", queueObject.bol)
        contentValues.put("comment", queueObject.comment)
        contentValues.put("path", queueObject.path)
        contentValues.put("status", queueObject.status)
        contentValues.put("gps", queueObject.gps)
        contentValues.put("doccode", queueObject.doccode)
        contentValues.put("picknum", queueObject.picknum)
        contentValues.put("signedby", queueObject.signedby)
        contentValues.put("exception", queueObject.exception)
        contentValues.put("account", queueObject.account)
        contentValues.put("pro", queueObject.pro)
        contentValues.put("cons", queueObject.cons)
        contentValues.put("conscity", queueObject.conscity)
        contentValues.put("consstate", queueObject.consstate)
        contentValues.put("pickup", queueObject.pickup)
        val bool = db.insert("img", null, contentValues)
        Log.d("Image ", "inserted--------")
        return true
    }

    fun getAllCaptureImages(): ArrayList<Bitmap> {
        val c: Cursor = readableDatabase.rawQuery("SELECT imagedata FROM img", null)
        val bitmapList = arrayListOf<Bitmap>()
        if (c.count > 0) {
            while (c.moveToNext()) {
                val bytes = c.getBlob(c.getColumnIndex("path"))
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                bitmapList.add(bitmap)
            }
            Log.e("total images size", "" + bitmapList.toString())
        }
        c.close()
        return bitmapList
    }


    fun deleteAllDatabases() {
        val db = this.writableDatabase
        val tables: MutableList<String> =
            ArrayList()
        tables.add("gps")
        tables.add("img")
        tables.add("shipments")
        for (table in tables) {
            val dropquery = "DROP TABLE IF EXISTS $table"
            db.execSQL(dropquery)
        }
        onCreate(db)
    }

    fun addShipment(
        sbolnum: String?,
        txdate: String?,
        shipdate: String?,
        srclocation: String?,
        doccode: String?,
        picknum: String?,
        delvnum: String?,
        bolnum: String?,
        shipnum: String?,
        shipper: String?,
        shipadd1: String?,
        shipadd2: String?,
        shipcity: String?,
        shipstate: String?,
        shipzip: String?,
        shipcntry: String?,
        shipacc: String?,
        consignee: String?,
        consadd1: String?,
        consadd2: String?,
        conscity: String?,
        consstate: String?,
        conszip: String?,
        conscntry: String?,
        consattn: String?,
        consacc: String?,
        hazmat: String?,
        hmcontact: String?,
        spinstr: String?,
        stopnum: String?,
        status: String?,
        downloaddate: String?,
        token: String?,
        path: String?,
        pronum: String?,
        comments: String?,
        pickup: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sbolnum", sbolnum)
        contentValues.put("txdate", txdate)
        contentValues.put("shipdate", shipdate)
        contentValues.put("srclocation", srclocation)
        contentValues.put("doccode", doccode)
        contentValues.put("picknum", picknum)
        contentValues.put("delvnum", delvnum)
        contentValues.put("bolnum", bolnum)
        contentValues.put("shipnum", shipnum)
        contentValues.put("shipper", shipper)
        contentValues.put("shipadd1", shipadd1)
        contentValues.put("shipadd2", shipadd2)
        contentValues.put("shipcity", shipcity)
        contentValues.put("shipstate", shipstate)
        contentValues.put("shipzip", shipzip)
        contentValues.put("shipcntry", shipcntry)
        contentValues.put("shipacc", shipacc)
        contentValues.put("consignee", consignee)
        contentValues.put("consadd1", consadd1)
        contentValues.put("consadd2", consadd2)
        contentValues.put("conscity", conscity)
        contentValues.put("consstate", consstate)
        contentValues.put("conszip", conszip)
        contentValues.put("conscntry", conscntry)
        contentValues.put("consattn", consattn)
        contentValues.put("consacc", consacc)
        contentValues.put("hazmat", hazmat)
        contentValues.put("hmcontact", hmcontact)
        contentValues.put("spinstr", spinstr)
        contentValues.put("stopnum", stopnum)
        contentValues.put("status", status)
        contentValues.put("downloaddate", downloaddate)
        contentValues.put("token", token)
        contentValues.put("path", path)
        contentValues.put("pronum", pronum)
        contentValues.put("comments", comments)
        contentValues.put("pickup", pickup)
        val ins = db.insert("shipments", null, contentValues)
        Log.e("Neha------", "inserted")
        return true
    }

    fun updateShipment(
        sbolnum: String,
        txdate: String?,
        shipdate: String?,
        srclocation: String?,
        doccode: String?,
        picknum: String?,
        delvnum: String?,
        bolnum: String?,
        shipnum: String?,
        shipper: String?,
        shipadd1: String?,
        shipadd2: String?,
        shipcity: String?,
        shipstate: String?,
        shipzip: String?,
        shipcntry: String?,
        shipacc: String?,
        consignee: String?,
        consadd1: String?,
        consadd2: String?,
        conscity: String?,
        consstate: String?,
        conszip: String?,
        conscntry: String?,
        consattn: String?,
        consacc: String?,
        hazmat: String?,
        hmcontact: String?,
        spinstr: String?,
        stopnum: String?,
        status: String?,
        downloaddate: String?,
        token: String?,
        path: String?,
        pronum: String?,
        comments: String?,
        pickup: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sbolnum", sbolnum)
        contentValues.put("txdate", txdate)
        contentValues.put("shipdate", shipdate)
        contentValues.put("srclocation", srclocation)
        contentValues.put("doccode", doccode)
        contentValues.put("picknum", picknum)
        contentValues.put("delvnum", delvnum)
        contentValues.put("bolnum", bolnum)
        contentValues.put("shipnum", shipnum)
        contentValues.put("shipper", shipper)
        contentValues.put("shipadd1", shipadd1)
        contentValues.put("shipadd2", shipadd2)
        contentValues.put("shipcity", shipcity)
        contentValues.put("shipstate", shipstate)
        contentValues.put("shipzip", shipzip)
        contentValues.put("shipcntry", shipcntry)
        contentValues.put("shipacc", shipacc)
        contentValues.put("consignee", consignee)
        contentValues.put("consadd1", consadd1)
        contentValues.put("consadd2", consadd2)
        contentValues.put("conscity", conscity)
        contentValues.put("consstate", consstate)
        contentValues.put("conszip", conszip)
        contentValues.put("conscntry", conscntry)
        contentValues.put("consattn", consattn)
        contentValues.put("consacc", consacc)
        contentValues.put("hazmat", hazmat)
        contentValues.put("hmcontact", hmcontact)
        contentValues.put("spinstr", spinstr)
        contentValues.put("stopnum", stopnum)
        contentValues.put("status", status)
        contentValues.put("downloaddate", downloaddate)
        contentValues.put("token", token)
        contentValues.put("path", path)
        contentValues.put("pronum", pronum)
        contentValues.put("comments", comments)
        contentValues.put("pickup", pickup)
        db.update("shipments", contentValues, "sbolnum = ?", arrayOf(sbolnum))
        return true
    }

    fun addUnsignedBOL(
        type: String?,
        sbolnum: String?,
        bol: String?,
        downloaddate: String?,
        path: String?,
        picknum: String?,
        status: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("type", type)
        contentValues.put("sbolnum", sbolnum)
        contentValues.put("bol", bol)
        contentValues.put("timestamp", downloaddate)
        contentValues.put("path", path)
        contentValues.put("picknum", picknum)
        contentValues.put("status", status)
        contentValues.put("token", "")

        contentValues.put("shipment", "")
        contentValues.put("trailer", "")

        contentValues.put("comment", "")

        contentValues.put("gps", "")
        contentValues.put("doccode", "")

        contentValues.put("signedby", "")
        contentValues.put("exception", "")
        contentValues.put("account", "P")
        contentValues.put("pro", "")
        contentValues.put("cons", "")
        contentValues.put("conscity", "")
        contentValues.put("consstate", "")
        contentValues.put("pickup", "")
        db.insert("img", null, contentValues)
        return true
    }

    fun getSignedPDF(token: String): HashMap<*, *>? {
        val sql =
            "select picknum, sbolnum, bol, path from img where type = 'pdf' and token = '$token'"
        val db = this.readableDatabase
        val picks = ArrayList<String>()
        val sbols = ArrayList<String>()
        val bols = ArrayList<String>()
        val pdfs = ArrayList<String>()
        val cursor = db.rawQuery(sql, null)
        val map: HashMap<String?, ArrayList<String>?> =
            HashMap<String?, ArrayList<String>?>()
        if (cursor.moveToLast()) {
            do {
                val pick = cursor.getString(0)
                val sbol = cursor.getString(1)
                val bol = cursor.getString(2)
                val path = cursor.getString(3)
                picks.add(pick)
                sbols.add(sbol)
                bols.add(bol)
                pdfs.add(path)
                map["picks"] = picks
                map["sbols"] = sbols
                map["bols"] = bols
                map["pdfs"] = pdfs
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return map
    }

    fun getBarcodePDF(picknum: String): HashMap<*, *>? {
        val sql =
            "select picknum, sbolnum, bol, path from images where type = 'pdf' and picknum = '$picknum'"
        val db = this.readableDatabase
        val picks = ArrayList<String>()
        val sbols = ArrayList<String>()
        val bols = ArrayList<String>()
        val pdfs = ArrayList<String>()
        val cursor = db.rawQuery(sql, null)
        val map: HashMap<String?, ArrayList<String>?> =
            HashMap<String?, ArrayList<String>?>()
        if (cursor.moveToLast()) {
            do {
                val pick = cursor.getString(0)
                val sbol = cursor.getString(1)
                val bol = cursor.getString(2)
                val path = cursor.getString(3)
                picks.add(pick)
                sbols.add(sbol)
                bols.add(bol)
                pdfs.add(path)
                map["picks"] = picks
                map["sbols"] = sbols
                map["bols"] = bols
                map["pdfs"] = pdfs
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return map
    }

    fun getPreviousVersions(sbolnum: String): HashMap<*, *>? {
        val sql =
           // "select picknum, sbolnum, bol, path from img where (type = 'pdf' or type = 'pod') and sbolnum = '$sbolnum' order by picknum asc"
            "select picknum, sbolnum, bol, path from img where (type = 'pdf' ) and sbolnum = '$sbolnum' order by picknum asc"
        val db = this.readableDatabase
        val picks = ArrayList<String>()
        val sbols = ArrayList<String>()
        val bols = ArrayList<String>()
        val pdfs = ArrayList<String>()
        val cursor = db.rawQuery(sql, null)
        val map: HashMap<String?, ArrayList<String>?> =
            HashMap<String?, ArrayList<String>?>()
        if (cursor.moveToLast()) {
            do {
                val picknum = cursor.getString(0)
                val sbol = cursor.getString(1)
                val bol = cursor.getString(2)
                val path = cursor.getString(3)
                picks.add(picknum)
                sbols.add(sbol)
                bols.add(bol)
                pdfs.add(path)
                map["picks"] = picks
                map["sbols"] = sbols
                map["bols"] = bols
                map["pdfs"] = pdfs
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return map
    }

    fun getActivePDF(): HashMap<*, *>? {
        val sql =
            "select picknum, sbolnum, bol, path from img where token = '' and  length(path) > 1 and type = 'pdf' and status = 'n'"
        val db = this.readableDatabase
        val picks = java.util.ArrayList<String>()
        val sbols = java.util.ArrayList<String>()
        val bols = java.util.ArrayList<String>()
        val pdfs = java.util.ArrayList<String>()
        val cursor = db.rawQuery(sql, null)
        val map: HashMap<Any?, Any?> = HashMap<Any?, Any?>()

        if (cursor.moveToLast()) {
            do {
                val picknum = cursor.getString(0)
                val sbol = cursor.getString(1)
                val bol = cursor.getString(2)
                val path = cursor.getString(3)
                picks.add(picknum)
                sbols.add(sbol)
                bols.add(bol)
                pdfs.add(path)
                map["picks"] = picks
                map["sbols"] = sbols
                map["bols"] = bols
                map["pdfs"] = pdfs
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return map
    }

    fun deleteRecordsFromDb() {
        val db = this.writableDatabase
//        val tables: MutableList<String> = java.util.ArrayList()
//        tables.add("gps")
//        tables.add("images")
//        tables.add("shipments")
        db.execSQL("delete from " + "img");
        db.execSQL("delete from " + "shipments");
    }

 ///////


   /* val c: Cursor = readableDatabase.rawQuery("SELECT imagedata FROM img", null)
    val bitmapList = arrayListOf<Bitmap>()
    if (c.count > 0) {
        while (c.moveToNext()) {
            val bytes = c.getBlob(c.getColumnIndex("imagedata"))
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bitmapList.add(bitmap)
        }
        Log.e("total images size", "" + bitmapList.toString())
    }
    c.close()
    return bitmapList
    */


    fun getImages(sbolnum: String): ArrayList<String>? {
        val images = arrayListOf<String>()
       // val sql = "select path from img where type = 'img' and sbolnum = '$sbolnum'"
       // val sql = "select path from img where type = 'pod' and sbolnum = '$sbolnum'"
       // (type = 'pdf' or type = 'pod')
        val sql = "select path from img where (type = 'img' or type = 'pod') and sbolnum = '$sbolnum'"
       // val sql = "select path from images where type = 'img' and sbolnum = '$sbolnum'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
//                val imageBytes =Base64.getDecoder()
//                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
             //   images.add(cursor.getString(0))
                val bytes = cursor.getString(cursor.getColumnIndex("path"))
              //  val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                images.add(bytes)
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return images
    }

    fun getDelPODImages(sbolnum: String): ArrayList<String>? {
        val images = arrayListOf<String>()
        // val sql = "select path from img where type = 'img' and sbolnum = '$sbolnum'"
        // val sql = "select path from img where type = 'pod' and sbolnum = '$sbolnum'"
        // (type = 'pdf' or type = 'pod')
        val sql = "select path from img where  type = 'pod' and sbolnum = '$sbolnum'"
        // val sql = "select path from images where type = 'img' and sbolnum = '$sbolnum'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
//                val imageBytes =Base64.getDecoder()
//                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                //   images.add(cursor.getString(0))
                val bytes = cursor.getString(cursor.getColumnIndex("path"))
                //  val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                images.add(bytes)
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return images
    }

    fun getDelPhotoImages(sbolnum: String): ArrayList<String>? {
        val images = arrayListOf<String>()
        // val sql = "select path from img where type = 'img' and sbolnum = '$sbolnum'"
        // val sql = "select path from img where type = 'pod' and sbolnum = '$sbolnum'"
        // (type = 'pdf' or type = 'pod')
        val sql = "select path from img where type = 'img'  and sbolnum = '$sbolnum'"
        // val sql = "select path from images where type = 'img' and sbolnum = '$sbolnum'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
//                val imageBytes =Base64.getDecoder()
//                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                //   images.add(cursor.getString(0))
                val bytes = cursor.getString(cursor.getColumnIndex("path"))
                //  val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                images.add(bytes)
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return images
    }


    fun countSBOLNum(sbolnum: String): Int {
        val sql = "select count(*) from shipments where sbolnum = '$sbolnum'"
        var count = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun getSbolNum(bolnum: String, shipnum: String): String? {
        var sbolnum = ""
        val sql =
            "select sbolnum from shipments where bolnum = '$bolnum' and shipnum = '$shipnum'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                sbolnum = cursor.getString(0)
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return sbolnum
    }

    fun getPickNum(sbolnum: String): String? {
        var picknum = ""
        val sql = "select picknum from shipments where sbolnum = '$sbolnum'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                picknum = cursor.getString(0)
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return picknum
    }
/*
    fun listAllDeliveries(): List<DeliveryObject>? {
        val sql = "select * from shipments where pickup='D'"
        val db = this.readableDatabase
        val listOfDeliveries: MutableList<DeliveryObject> =
            ArrayList<DeliveryObject>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0).toInt()
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfDeliveries.add(
                    DeliveryObject(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfDeliveries
    }

    fun listPendingDeliveries(): List<DeliveryObject>? {
        val sql = "select * from shipments where pickup='D' and status = 'n'"
        val db = this.readableDatabase
        val listOfDeliveries: MutableList<DeliveryObject> =
            ArrayList<DeliveryObject>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfDeliveries.add(
                    DeliveryObject(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfDeliveries
    }

    fun listSignedDeliveries(): List<DeliveryObject>? {
        val sql = "select * from shipments where pickup='D' and status = 'y'"
        val db = this.readableDatabase
        val listOfDeliveries: MutableList<DeliveryObject> =
            ArrayList<DeliveryObject>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfDeliveries.add(
                    DeliveryObject(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfDeliveries
    }

    fun listProcessedOrDownloadedDeliveries(): List<DeliveryObject>? {
        val sql = "select * from shipments where pickup='D' and status like 'yy%'"
        val db = this.readableDatabase
        val listOfDeliveries: MutableList<DeliveryObject> =
            ArrayList<DeliveryObject>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfDeliveries.add(
                    DeliveryObject(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfDeliveries
    }

    fun listDownloaedDeliveries(): List<DeliveryObject>? {
        val sql = "select * from shipments where pickup='D' and status = 'yyy'"
        val db = this.readableDatabase
        val listOfDeliveries: MutableList<DeliveryObject> =
            ArrayList<DeliveryObject>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfDeliveries.add(
                    DeliveryObject(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfDeliveries
    }*/

    fun listAllPickups(): List<PickupResponseNew>? {
        val sql = "select * from shipments where pickup='D'"
        val db = this.readableDatabase
        val listOfPickups: MutableList<PickupResponseNew> =
            ArrayList<PickupResponseNew>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0).toInt()
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfPickups.add(
                    PickupResponseNew(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup

                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfPickups
    }

    fun listPendingPickups(): List<PickupResponseNew>? {
        val sql = "select * from shipments where pickup='D' and status = 'n'"
        val db = this.readableDatabase
        val listOfPickups: MutableList<PickupResponseNew> =
            ArrayList<PickupResponseNew>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfPickups.add(
                    PickupResponseNew(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfPickups
    }

    fun listSignedPickups(): List<PickupResponseNew>? {
        val sql = "select * from shipments where pickup='D' and status = 'y'"
        val db = this.readableDatabase
        val listOfPickups: MutableList<PickupResponseNew> =
            ArrayList<PickupResponseNew>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfPickups.add(
                    PickupResponseNew(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfPickups
    }

    fun listProcessedOrDownloadedPickups(): List<PickupResponseNew>? {
        val sql = "select * from shipments where pickup='D' and status like 'yy%'"
        val db = this.readableDatabase
        val listOfPickups: MutableList<PickupResponseNew> =
            ArrayList<PickupResponseNew>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfPickups.add(
                    PickupResponseNew(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfPickups
    }

    fun listDownloaedPickups(): List<PickupResponseNew>? {
        val sql = "select * from shipments where pickup='P' and status = 'yyy'"
        val db = this.readableDatabase
        val listOfPickups: MutableList<PickupResponseNew> =
            ArrayList<PickupResponseNew>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0)
                val sbolnum = cursor.getString(1)
                val txdate = cursor.getString(2)
                val shipdate = cursor.getString(3)
                val srclocation = cursor.getString(4)
                val doccode = cursor.getString(5)
                val picknum = cursor.getString(6)
                val delvnum = cursor.getString(7)
                val bolnum = cursor.getString(8)
                val shipnum = cursor.getString(9)
                val shipper = cursor.getString(10)
                val shipadd1 = cursor.getString(11)
                val shipadd2 = cursor.getString(12)
                val shipcity = cursor.getString(13)
                val shipstate = cursor.getString(14)
                val shipzip = cursor.getString(15)
                val shipcntry = cursor.getString(16)
                val shipacc = cursor.getString(17)
                val consignee = cursor.getString(18)
                val consadd1 = cursor.getString(19)
                val consadd2 = cursor.getString(20)
                val conscity = cursor.getString(21)
                val consstate = cursor.getString(22)
                val conszip = cursor.getString(23)
                val conscntry = cursor.getString(24)
                val consattn = cursor.getString(25)
                val consacc = cursor.getString(26)
                val hazmat = cursor.getString(27)
                val hmcontact = cursor.getString(28)
                val spinstr = cursor.getString(29)
                val stopnum = cursor.getString(30)
                val status = cursor.getString(31)
                val downloaddate = cursor.getString(32)
                val token = cursor.getString(33)
                val path = cursor.getString(34)
                val pronum = cursor.getString(35)
                val comments = cursor.getString(36)
                val pickup = cursor.getString(37)
                listOfPickups.add(
                    PickupResponseNew(
                        sbolnum,
                        txdate,
                        shipdate,
                        srclocation,
                        doccode,
                        picknum,
                        delvnum,
                        bolnum,
                        shipnum,
                        shipper,
                        shipadd1,
                        shipadd2,
                        shipcity,
                        shipstate,
                        shipzip,
                        shipcntry,
                        shipacc,
                        consignee,
                        consadd1,
                        consadd2,
                        conscity,
                        consstate,
                        conszip,
                        conscntry,
                        consattn,
                        consacc,
                        hazmat,
                        hmcontact,
                        spinstr,
                        stopnum,
                        status,
                        downloaddate,
                        token,
                        path,
                        pronum,
                        comments,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfPickups
    }

    fun setDelvStatusTrue(sbolnum: String?, picknum: String?) {}
    fun deleteShipments(olddate: String): String? {
        val db = this.readableDatabase
        val sql1 =
            "select path from img where type = 'pdf' and timestamp < $olddate"
        val sql2 = "delete from shipments where downloaddate < $olddate"
        var result = ""
        val cursor1 = db.rawQuery(sql1, null)
        if (cursor1.moveToFirst()) {
            do {
                result = cursor1.getString(0)
                val path = File(result)
                if (path.exists()) {
                    if (path.delete()) {
                        println("file Deleted :$result")
                    } else {
                        println("file not Deleted :$result")
                    }
                }
            } while (cursor1.moveToNext())
        }
        cursor1.close()
        val cursor2 = db.rawQuery(sql2, null)
        if (cursor2.moveToFirst()) {
            do {
                result = cursor2.getString(0)
            } while (cursor2.moveToNext())
        }
        cursor2.close()
        return result

        //SQLiteDatabase db =  this.getWritableDatabase();
        //return db.delete("shipments", "signdate < NOW() - INTERVAL ? DAY", new String[] { numdays });
    }

    // add images
    open fun addImage(queueObject: QueueObject): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sbolnum", queueObject.sbolnum)
        contentValues.put("type", queueObject.type)
        contentValues.put("timestamp", queueObject.timestamp)
        contentValues.put("shipment", queueObject.shipment)
        contentValues.put("trailer", queueObject.trailer)
        contentValues.put("bol", queueObject.bol)
        contentValues.put("comment", queueObject.comment)
        contentValues.put("path", queueObject.path)
        contentValues.put("status", queueObject.status)
        contentValues.put("gps", queueObject.gps)
        contentValues.put("doccode", queueObject.doccode)
        contentValues.put("picknum", queueObject.picknum)
        contentValues.put("signedby", queueObject.signedby)
        contentValues.put("exception", queueObject.exception)
        contentValues.put("account", queueObject.account)
        contentValues.put("pro", queueObject.pro)
        contentValues.put("cons", queueObject.cons)
        contentValues.put("conscity", queueObject.conscity)
        contentValues.put("consstate", queueObject.consstate)
        contentValues.put("pickup", queueObject.pickup)
        val bool = db.insert("img", null, contentValues)
        Log.d("Image---- ", "inserted")
        return true
    }

    fun pendingImages(): List<QueueObject>? {
        val sql = "select * from img where status = 'n'"
        val db = this.readableDatabase
        val listOfImages: ArrayList<QueueObject> =
            java.util.ArrayList()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToLast()) {
            do {
                val id = cursor.getString(0).toInt()
                val type = cursor.getString(1)
                val timestamp = cursor.getString(2)
                val uploadtimestamp = getTimestamp()
                val path = cursor.getString(4)
                val status = cursor.getString(5)
                val shipment = cursor.getString(6)
                val trailer = cursor.getString(7)
                val bol = cursor.getString(8)
                val comment = cursor.getString(9)
                val sbolnum = cursor.getString(10)
                val signedby = cursor.getString(11)
                val signgps = cursor.getString(12)
                val signhash = cursor.getString(13)
                val signinfo = cursor.getString(14)
                val signimage = cursor.getString(15)
                val token = cursor.getString(16)
                val doccode = cursor.getString(17)
                val email = cursor.getString(18)
                val picknum = cursor.getString(19)
                val exception = cursor.getString(20)
                val account = cursor.getString(21)
                val pro = cursor.getString(22)
                val cons = cursor.getString(23)
                val conscity = cursor.getString(24)
                val consstate = cursor.getString(25)
                val pickup = cursor.getString(26)
                listOfImages.add(
                    QueueObject(
                        type,
                        timestamp,
                        uploadtimestamp!!,
                        path,
                        status,
                        shipment,
                        trailer,
                        bol,
                        comment,
                        sbolnum,
                        signedby,
                        signgps,
                        "signhash",
                        "signinfo",
                        "signimage",
                        "token",
                        doccode,
                        "email",
                        picknum,
                        exception,
                        account,
                        pro,
                        cons,
                        conscity,
                        consstate,
                        pickup
                    )
                )
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return listOfImages
    }


}