package com.example.smartboldriver.features.shipments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.smartboldriver.R
import com.google.android.material.tabs.TabLayout

class DelListActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    private var sbolnum: String? = null
    private var bolnum: String? = null
    private var picknum: String? = null
    private var type: String? = null
    private var doccode: String? = null
    private var pickup: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_list)

        val i = this.intent
        this.sbolnum = i.getStringExtra("sbol")
        this.bolnum = i.getStringExtra("bol")
        this.picknum = i.getStringExtra("picknum")
        this.type = i.getStringExtra("type")
        this.doccode = i.getStringExtra("doccode")

        title = "Delivery Documents"
        tabLayout = findViewById(R.id.tabLayout1)
        viewPager = findViewById(R.id.viewPager1)
        tabLayout.addTab(tabLayout.newTab().setText("Delivery Photo"))
        tabLayout.addTab(tabLayout.newTab().setText("POD"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = DelImageLIstAdapter(
            this, supportFragmentManager,
            tabLayout.tabCount,
            sbolnum!!
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
}