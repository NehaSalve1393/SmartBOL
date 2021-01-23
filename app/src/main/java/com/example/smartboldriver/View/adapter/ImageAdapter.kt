package com.example.smartboldriver.View.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.smartboldriver.R
import kotlinx.android.synthetic.main.image_slider.view.*


class ImageAdapter(private val context: Context) : PagerAdapter() {


    private var inflater: LayoutInflater? = null
    private val images = arrayOf(R.drawable.bol, R.drawable.bol, R.drawable.bol, R.drawable.bol)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {

        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.test, null)
        view.imageView_slide.setImageResource(images[position])

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}