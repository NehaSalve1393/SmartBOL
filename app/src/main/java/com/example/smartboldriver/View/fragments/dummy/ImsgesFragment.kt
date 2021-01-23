package com.example.smartboldriver.View.fragments.dummy

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartboldriver.R
import kotlinx.android.synthetic.main.images_fragment_layout.*

class ImsgesFragment constructor(val bitmapImgList:ArrayList<String>):Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.images_fragment_layout, container, false)
        view.setOnClickListener {  }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("image","in ImsgesFragment bitmapImgList = $bitmapImgList")
        images_pager.adapter = ImagesAdapter(context!!, bitmapImgList)
    }
}