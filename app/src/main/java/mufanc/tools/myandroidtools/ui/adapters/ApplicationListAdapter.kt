package mufanc.tools.myandroidtools.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ViewAppListBinding
import mufanc.tools.myandroidtools.ui.AppDetailActivity
import mufanc.tools.myandroidtools.utils.AppInfoHelper.AppInfo
import mufanc.tools.myandroidtools.utils.FilterHelper

class ApplicationListAdapter(
    private val appList: List<AppInfo>
) : Filterable, RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>() {

    private var filteredList: List<AppInfo> = listOf()

    init {
        filter.filter("")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIconHolder: ImageView
        val appUidHolder: TextView
        val appNameHolder: TextView
        val packageNameHolder: TextView

        init {
            ViewAppListBinding.bind(view).apply {
                appIconHolder = appIcon
                appUidHolder = appUid
                appNameHolder = appName
                packageNameHolder = packageName
            }
        }
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(filter: CharSequence): FilterResults {
            val results = mutableListOf<AppInfo>()
            for (info in appList) {
                if (FilterHelper.judge(info)) results.add(info)
            }
            FilterHelper.sort(results)
            return FilterResults().apply { values = results }
        }

        @Suppress("Unchecked_Cast")
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(filter: CharSequence, results: FilterResults) {
            filteredList = results.values as List<AppInfo>
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_app_list, parent, false)
            .let { return ViewHolder(it) }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = filteredList[position]
        with (holder) {
            appIconHolder.setImageDrawable(appInfo.iconCopy())
            appUidHolder.text = "(${appInfo.uid})"
            appNameHolder.text = appInfo.appName
            packageNameHolder.text = appInfo.packageName

            if (appInfo.enabled) {
                appIconHolder.colorFilter = null
                appNameHolder.alpha = 1f
            } else {
                appIconHolder.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                appNameHolder.alpha = 0.6f
            }

            itemView.setOnClickListener {
                Intent(itemView.context, AppDetailActivity::class.java).let {
                    it.putExtra("packageName", appInfo.packageName)
                    itemView.context.startActivity(it)
                }
            }
        }
    }

    override fun getItemCount() = filteredList.size
}