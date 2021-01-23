package com.example.smartboldriver.View.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.smartboldriver.R
import com.example.smartboldriver.models.pickup.PickupResponseNew
import com.example.smartboldriver.utils.DBHelper
import kotlinx.android.synthetic.main.fragment_delivery_item_list.*
import kotlinx.android.synthetic.main.fragment_delivery_item_list.view.*

/**
 * A fragment representing a list of Items.
 */
class DeliveryItemFragment : Fragment() {

    private var columnCount = 1
    private var type = ""
    //lateinit var db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            type = it.getString("type")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_item_list, container, false)
         val db = DBHelper(this.context);
       Log.e("Neha--Liost", db.listAllPickups().toString())

        var list = db.listAllPickups()
       list =  separateList(list, db)
       // Log.e("Neha--Length", list!![0].status.toString())

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
               // adapter = MyItemRecyclerViewAdapter(db.listAllPickups()!!,this.context,)
                adapter = MyItemRecyclerViewAdapter(list!!, this.context, fragmentManager!!)
            }
        }
//           view.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
//        view.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
//
//        view.itemsswipetorefresh.setOnRefreshListener {
//            if (view is RecyclerView) {
//                with(view) {
//                    layoutManager = when {
//                        columnCount <= 1 -> LinearLayoutManager(context)
//                        else -> GridLayoutManager(context, columnCount)
//                    }
//                    // adapter = MyItemRecyclerViewAdapter(db.listAllPickups()!!,this.context,)
//                    adapter = MyItemRecyclerViewAdapter(list!!, this.context, fragmentManager!!)
//                }
//            }
//            view.itemsswipetorefresh.isRefreshing = false
//        }


        return view
    }
    fun separateList(list: List<PickupResponseNew>?, db: DBHelper): List<PickupResponseNew> {
        var sList :List<PickupResponseNew>
        if(type.equals("C")){
            sList = db.listProcessedOrDownloadedPickups()!!
        }
        else if(type.equals("P")){
            sList = db.listPendingPickups()!!
        }
        else if(type.equals("S")){
            sList = db.listSignedPickups()!!
        }
        else{
            sList = db.listAllPickups()!!
        }
        return sList
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, s: String) =
            DeliveryItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString("type", s)
                }
            }
    }

    /*override fun onBtnClick(position: Int) {
       Log.e("Neha.....Button","click")
    }*/
}