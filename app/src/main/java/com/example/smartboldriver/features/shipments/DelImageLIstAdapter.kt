package com.example.smartboldriver.features.shipments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


internal class DelImageLIstAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var sbolnum: String
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DelImagesFragment.newInstance(0,"Image",sbolnum)
            }
            1 -> {
                DelImagesFragment.newInstance(0,"POD",sbolnum)
            }

            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}