package mufanc.tools.myandroidtools.ui

import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import mufanc.tools.myandroidtools.MyApplication
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ActivityAppDetailBinding
import mufanc.tools.myandroidtools.ui.adapters.AppDetailViewPagerAdapter
import mufanc.tools.myandroidtools.utils.AppInfoHelper

class AppDetailActivity : AppCompatActivity() {
    enum class ComponentTab(private val type: Int, val title: Int) {
        TYPE_ACTIVITY(PackageManager.GET_ACTIVITIES, R.string.tab_activities),
        TYPE_RECEIVER(PackageManager.GET_RECEIVERS, R.string.tab_receivers),
        TYPE_SERVICE(PackageManager.GET_SERVICES, R.string.tab_services),
        TYPE_PROVIDER(PackageManager.GET_PROVIDERS, R.string.tab_providers);

        fun getComponentList(packageName: String) = AppInfoHelper.getComponentList(packageName, type)
    }

    private lateinit var adapter: AppDetailViewPagerAdapter

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        val packageName = intent.getStringExtra("packageName") ?: "android"

        val binding = ActivityAppDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            val appInfo = AppInfoHelper.getAppInfo(packageName)
            appIcon.setImageDrawable(appInfo.icon)
            appName.text = appInfo.appName

                AppDetailViewPagerAdapter(
                this@AppDetailActivity, packageName, ComponentTab.values()
            ).let {
                adapter = it
                viewPager.adapter = it
            }

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.setText(if (position == 0) {
                    R.string.tab_overview
                } else {
                    ComponentTab.values()[position - 1].title
                })
            }.attach()
        }
    }
}