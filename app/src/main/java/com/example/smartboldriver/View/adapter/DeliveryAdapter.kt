import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartboldriver.View.fragments.DeliveryItemFragment

@Suppress("DEPRECATION")
internal class DeliveryAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
               DeliveryItemFragment.newInstance(0,"A")
            }
            1 -> {
                DeliveryItemFragment.newInstance(0,"P")
            }
            2 -> {
                DeliveryItemFragment.newInstance(0,"S")
            }
            3 -> {
                DeliveryItemFragment.newInstance(0,"C")
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}