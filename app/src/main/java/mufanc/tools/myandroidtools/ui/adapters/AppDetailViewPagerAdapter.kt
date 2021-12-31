package mufanc.tools.myandroidtools.ui.adapters

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import mufanc.tools.myandroidtools.ui.AppDetailActivity
import mufanc.tools.myandroidtools.ui.fragments.AppDetailFragment
import mufanc.tools.myandroidtools.ui.fragments.ComponentListFragment
import java.lang.ref.WeakReference

class AppDetailViewPagerAdapter(
    private val activity: AppCompatActivity,
    private val packageName: String,
    private val componentTabs: Array<AppDetailActivity.ComponentTab>
) : FragmentStateAdapter(activity) {
    private val fragmentCache = mutableMapOf<Int, WeakReference<Fragment>>()

    override fun getItemCount() = componentTabs.size + 1

    fun getItem(position: Int) = createFragment(position)

    override fun createFragment(position: Int): Fragment {
        fragmentCache[position]?.get()?.let { return it }

        if (position == 0) {
            AppDetailFragment(packageName)
        } else {
            ComponentListFragment(packageName, componentTabs[position - 1], position)
        }.let {
            fragmentCache[position] = WeakReference(it)
            return it
        }
    }
}