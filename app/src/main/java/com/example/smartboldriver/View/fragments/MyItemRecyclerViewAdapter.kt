package com.example.smartboldriver.View.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartboldriver.R
import com.example.smartboldriver.View.PODBlankPageActivity
import com.example.smartboldriver.View.PdfViewerActivity
import com.example.smartboldriver.View.fragments.dummy.DummyContent.DummyItem
import com.example.smartboldriver.View.fragments.dummy.ImsgesFragment
import com.example.smartboldriver.features.shipments.DelListActivity
import com.example.smartboldriver.features.shipments.DelSignActivity
import com.example.smartboldriver.features.shipments.DeliveryListActivity
import com.example.smartboldriver.models.pickup.PickupResponseNew
import com.example.smartboldriver.utils.DBHelper
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<PickupResponseNew>,
    val context: Context,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    companion object {
        var mClickListener: BtnClickListener? = null
    }
    val db = DBHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_delivery_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.consignee.text = item.consignee
        holder.con_add1.text = item.consadd1
        holder.cons_add2.text = item.consadd2 +" "+item.conscity+" "+item.consstate+" "+item.conscntry+" "+item.conszip
        holder.from_val_txt.text = item.shipper
        holder.spl_inst_txt.text = item.spinstr
        holder.tvAttn.text = item.consattn

        if(item.doccode.equals("norec")){
            holder.sign_img.visibility = View.GONE
            holder.sign_txt.visibility = View.GONE
        }


        if (item.status.equals("yyy")) {
            holder.tv_status.visibility = View.VISIBLE
            holder.tv_status.text = "Complete"
            holder.tv_status.setTextColor(Color.BLUE)
        } else if (item.status.equals("yy")) {
            holder.tv_status.visibility = View.VISIBLE
            holder.tv_status.text = "Uploaded"
            holder.tv_status.setTextColor(Color.DKGRAY)
        } else if (item.status.equals("y")) {
            holder.tv_status.visibility = View.VISIBLE
            holder.tv_status.text = "Signed"
            holder.tv_status.setTextColor(Color.RED)
        } else holder.tv_status.visibility = View.GONE



//        holder.consignee.text = "item.consignee"
//        holder.con_add1.text = "item.consadd1"

       holder.gallery.setOnClickListener{
           val bitmapImgList = db.getImages(item.sbolnum!!)
          // val bitmapImgList = db.getAllCaptureImages()
           if (bitmapImgList!!.size>0){
//               val fragment = ImsgesFragment(bitmapImgList!!)
//               fragmentManager
//                   .beginTransaction()
//                   .add(R.id.frame_lay, fragment)
//                   .addToBackStack("")
//                   .commit()


               val intent =
                   Intent(context, DelListActivity::class.java)
               intent.putExtra("sbol", item.sbolnum)
               intent.putExtra("picknum", item.picknum)
               intent.putExtra("bol", item.bolnum)
               intent.putExtra("type", "pod")
               intent.putExtra("doccode", "rec")
               intent.putExtra("pickup", "D")
               intent.addFlags(
                   Intent.FLAG_ACTIVITY_NO_ANIMATION or
                           Intent.FLAG_ACTIVITY_CLEAR_TOP or
                           Intent.FLAG_ACTIVITY_NEW_TASK
               )
               context.startActivity(intent)
           }
           else Toast.makeText(context, "Images not found", Toast.LENGTH_SHORT).show()

       }
        holder.camera.setOnClickListener{
            (context as DeliveryListActivity).takePicture(item)

          //  startActivityForResult(context,intent, Bundle())
        }
        holder.pod_img.setOnClickListener{
           // (context as DeliveryListActivity).takePicture(item)

            //  startActivityForResult(context,intent, Bundle())

            if(item.status == "y" || item.status=="yyy"){
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Warning!")
                builder.setMessage("This delivery has been signed. Do you want to resign ?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    val intent =
                        Intent(context, PODBlankPageActivity::class.java)
                    intent.putExtra("sbol", item.sbolnum)
                    intent.putExtra("picknum", item.picknum)
                    intent.putExtra("bol", item.bolnum)
                    intent.putExtra("type", "pod")
                    intent.putExtra("doccode", "rec")
                    intent.putExtra("pickup", "D")

                    var isAlreadySign:Boolean = false
                    var title:String=""

                    intent.putExtra("isAlreadySign",isAlreadySign )
                    intent.putExtra("Signed copy",title )
                    intent.putExtra("status", item.status)
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                    context.startActivity(intent)

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
            else{
                val intent =
                    Intent(context, PODBlankPageActivity::class.java)
                intent.putExtra("sbol", item.sbolnum)
                intent.putExtra("picknum", item.picknum)
                intent.putExtra("bol", item.bolnum)
                intent.putExtra("type", "pod")
                intent.putExtra("doccode", "rec")
                intent.putExtra("pickup", "D")

                var isAlreadySign:Boolean = false
                var title:String=""

                intent.putExtra("isAlreadySign",isAlreadySign )
                intent.putExtra("Signed copy",title )
                intent.putExtra("status", item.status)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
                context.startActivity(intent)
            }


        }
        holder.sign_img.setOnClickListener(){
           /* val intent = Intent(context, ActiveBOLActivity::class.java)
            startActivity(context,intent,Bundle());*/

            val picknums = ArrayList<String>()
            val sbolnums = ArrayList<String>()
            val bolnums = ArrayList<String>()
            val pdfs = ArrayList<String>()
            picknums.add(item.picknum!!)
            sbolnums.add(item.sbolnum!!)
            bolnums.add(item.bolnum!!)
            pdfs.add(item.path!!)

          //  holder.select.setVisibility(View.INVISIBLE)
          //  delivery.setSelected(false)

            val intent =
                Intent(context, PdfViewerActivity::class.java)
            //intent.putStringArrayListExtra("multidelv", delvnum);
            //intent.putStringArrayListExtra("multidelv", delvnum);
            intent.putStringArrayListExtra("multipick", picknums)
            intent.putStringArrayListExtra("multisbol", sbolnums)
            intent.putStringArrayListExtra("multibol", bolnums)
            intent.putStringArrayListExtra("multipdfs", pdfs)
            intent.putExtra("pickup", "D")
            var isAlreadySign:Boolean = false
            var title:String=""
            if(item.status == "y" || item.status=="yyy"){
                isAlreadySign = true
                title = "Signed copy"
                }
            intent.putExtra("isAlreadySign",isAlreadySign )
            intent.putExtra("Signed copy",title )
            intent.putExtra("status", item.status)


            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        if(item.doccode == "norec"){

            holder.sign_grp.visibility = View.GONE
        }
    }


    //override fun getItemCount(): Int = 1
    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val gallery: ImageView = view.findViewById(R.id.image_img)
        val camera: ImageView = view.findViewById(R.id.photo_img)
        val pod_img: ImageView = view.findViewById(R.id.pod_img)
        val sign_img: ImageView = view.findViewById(R.id.sign_img11)
        val consignee: TextView = view.findViewById(R.id.cons_txt1)
        val con_add1: TextView = view.findViewById(R.id.cons_add1)
        val cons_add2: TextView = view.findViewById(R.id.cons_add2)
        val tv_status: TextView = view.findViewById(R.id.tv_status)
        val from_val_txt: TextView = view.findViewById(R.id.from_val_txt)
        val spl_inst_txt: TextView = view.findViewById(R.id.spl_inst_txt)
        val sign_txt: TextView = view.findViewById(R.id.sign_txt11)
        val tvAttn: TextView = view.findViewById(R.id.tvAttn)
        val sign_grp : Group = view.findViewById(R.id.sign_group)


//        override fun toString(): String {
//            //return super.toString() + " '" + contentView.text + "'"
//        }
    }


    open interface BtnClickListener {
        fun onBtnClick(position: Int)
    }
}