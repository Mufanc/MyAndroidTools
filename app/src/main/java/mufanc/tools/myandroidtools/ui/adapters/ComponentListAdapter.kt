package mufanc.tools.myandroidtools.ui.adapters

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ViewComponentListBinding
import mufanc.tools.myandroidtools.utils.AppInfoHelper.CompInfo
import mufanc.tools.myandroidtools.utils.ComponentManager

class ComponentListAdapter(
    private val packageName: String,
    private val componentList: List<CompInfo>
) : Filterable, RecyclerView.Adapter<ComponentListAdapter.ViewHolder>() {
    private var filteredList: List<CompInfo> = listOf()

    init {
        filter.filter("")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentNameHolder: TextView
        val isEnabledHolder: SwitchMaterial

        init {
            ViewComponentListBinding.bind(view).apply {
                componentNameHolder = componentName
                isEnabledHolder = enabled
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_component_list, parent, false)
            .let { return ViewHolder(it) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val compInfo = filteredList[position]
        with (holder) {
            componentNameHolder.text = compInfo.simpleName
            isEnabledHolder.isChecked = compInfo.enabled
            
            isEnabledHolder.setOnCheckedChangeListener { _, enabled ->
                ComponentManager.setComponentEnabledSetting(
                    ComponentName(packageName, compInfo.name),
                    if (enabled) {
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    } else {
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    }
                )
            }
        }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter() = object : Filter() {
        override fun performFiltering(query: CharSequence): FilterResults {
            val results = mutableListOf<CompInfo>()
            for (info in componentList) {
                if (query == "") results.add(info)
                else if (info.simpleName.lowercase()
                        .contains(query.toString().lowercase())) {
                    results.add(info)
                }
            }
            results.sortWith { o1, o2 ->
                val c1 = o1.enabled
                val c2 = o2.enabled
                if (c1 != c2) return@sortWith if (c1) 1 else -1
                o1.simpleName.compareTo(o2.simpleName)
            }
            return FilterResults().apply { values = results }
        }

        @Suppress("Unchecked_Cast")
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(filter: CharSequence, results: FilterResults) {
            filteredList = results.values as List<CompInfo>
            notifyDataSetChanged()
        }
    }
}