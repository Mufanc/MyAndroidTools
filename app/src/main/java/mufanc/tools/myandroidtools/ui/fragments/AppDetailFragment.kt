package mufanc.tools.myandroidtools.ui.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.switchmaterial.SwitchMaterial
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.FragmentAppDetailBinding
import mufanc.tools.myandroidtools.utils.AppInfoHelper
import mufanc.tools.myandroidtools.utils.ComponentManager

class AppDetailFragment(private val packageName: String) : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding = FragmentAppDetailBinding.inflate(layoutInflater)

        val appInfo = AppInfoHelper.getAppInfo(packageName)
        val packageInfo = AppInfoHelper.packageManager.getPackageInfo(packageName, 0)

        with (binding) {
            appIcon.setImageDrawable(appInfo.icon)
            appName.text = appInfo.appName
            appPackageName.text = appInfo.packageName
            appUid.text = "UID: ${appInfo.uid}"
            appApkPath.text = packageInfo.applicationInfo.sourceDir
            appVersion.text = "${packageInfo.versionName} (${packageInfo.longVersionCode})"

            setClipOnClick(arrayOf(
                appName, appPackageName, appApkPath
            ))
        }

        return binding.root
    }

    private fun setClipOnClick(list: Array<TextView>) {
        list.forEach { view ->
            view.setOnClickListener {
                val manager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                manager.setPrimaryClip(ClipData.newPlainText("", view.text))
                Toast.makeText(context, R.string.hint_clipboard_set, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity()
            .findViewById<ViewPager2>(R.id.view_pager)
            .currentItem.let { if (it != 0) return }

        inflater.inflate(R.menu.fragment_app_detail_menu, menu)

        menu.findItem(R.id.app_switch).let {
            it.setActionView(R.layout.view_switch)
            it.actionView.findViewById<SwitchMaterial>(R.id.app_switch_button).apply {
                isChecked = AppInfoHelper.getAppInfo(packageName).enabled
                setOnCheckedChangeListener { _, enabled ->
                    ComponentManager.setApplicationEnabledSetting(
                        packageName,
                        if (enabled) {
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                        } else {
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                        }
                    )
                    AppInfoHelper.updateAppInfo(packageName)
                }
            }
        }
    }
}