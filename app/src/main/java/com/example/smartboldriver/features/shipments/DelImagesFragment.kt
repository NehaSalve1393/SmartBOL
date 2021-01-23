import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartboldriver.R
import com.example.smartboldriver.View.fragments.DeliveryItemFragment
import com.example.smartboldriver.View.fragments.dummy.ImagesAdapter
import com.example.smartboldriver.models.pickup.PickupResponseNew
import com.example.smartboldriver.utils.DBHelper
import kotlinx.android.synthetic.main.images_fragment_layout.*

class DelImagesFragment constructor(): Fragment(){
    private var columnCount = 1
    private var type = ""
    private var sbolNumber = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.images_fragment_layout, container, false)
        view.setOnClickListener {  }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(DeliveryItemFragment.ARG_COLUMN_COUNT)
            type = it.getString("type")!!
            sbolNumber = it.getString("sbol")!!

        }
    }

    fun separateList( db: DBHelper): List<String> {
        var sList :List<String>

        if(type.equals("Image")){
            sList = db.getDelPhotoImages(sbolNumber)!!
        }
        else {
            sList = db.getDelPODImages(sbolNumber)!!
        }


        return sList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.e("image","in ImsgesFragment bitmapImgList = $bitmapImgList")
        var db = DBHelper(context)
        val bitmapImgList = separateList(db)

        images_pager.adapter = ImagesAdapter(context!!, bitmapImgList as ArrayList<String>)
    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, s: String, sbolnum: Any?) =
            DelImagesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString("type",s)
                    putString("sbol", (sbolnum as String?)!!)
                }
            }
    }

}