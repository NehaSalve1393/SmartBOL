package com.example.smartboldriver.View.fragments.dummy

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.smartboldriver.R
import kotlin.math.max
import kotlin.math.min

@Suppress("DEPRECATION")
internal class ImagesAdapter(
    var context: Context,
    private val bitmapArr: ArrayList<String>
) :
    PagerAdapter() {
   /* override fun getItem(position: Int): Fragment {
        return ImagesItemFragment(bitmapArr[position])
    }*/

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.image_lay, container, false)
        val imageView: ImageView = view.findViewById(R.id.cap_img_view)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
      //  imageView.setImageBitmap(bitmapArr.get(position))
         val targetW: Int = imageView!!.width
         val targetH: Int = imageView!!.height
        var count = position+1
        tvCount.text = count.toString() +" /"+bitmapArr.size

        val vto: ViewTreeObserver = imageView.getViewTreeObserver()
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this)
               var  finalHeight = imageView.getMeasuredHeight()
               var   finalWidth = imageView.getMeasuredWidth()
               // tv.setText("Height: " + finalHeight.toString() + " Width: " + finalWidth)
                return true
            }
        })

        setPic(imageView, bitmapArr.get(position))
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
    override fun getCount(): Int {
        return bitmapArr.size
    }

    private fun setPic(mImageView: ImageView, get: String) {
        Log.e("Nisha---",bitmapArr.toString())


        // Get the dimensions of the View
      //  val targetW: Int = mImageView!!.width
        val targetW: Int = 300
     //  val targetH: Int = mImageView!!.height
        val targetH: Int = 500

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(get, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = max(1, min(photoW / targetW, photoH / targetH))

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true
        val bitmap = BitmapFactory.decodeFile(get, bmOptions)
        mImageView!!.setImageBitmap(bitmap)
    }
}