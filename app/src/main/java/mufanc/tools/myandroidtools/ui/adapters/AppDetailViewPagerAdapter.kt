package mufanc.tools.myandroidtools.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import mufanc.tools.myandroidtools.ui.AppDetailActivity
import mufanc.tools.myandroidtools.ui.fragments.AppDetailFragment
import mufanc.tools.myandroidtools.ui.fragments.ComponentListFragment

class AppDetailViewPagerAdapter(
    private val activity: AppCompatActivity,
    private val packageName: String,
    private val componentTabs: Array<AppDetailActivity.ComponentTab>
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = componentTabs.size + 1

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            AppDetailFragment(packageName)
        } else {
            ComponentListFragment(packageName, componentTabs[position - 1], position)
        }
    }
}