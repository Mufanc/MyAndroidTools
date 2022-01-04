package mufanc.tools.myandroidtools.ui.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ViewProcessListBinding
import mufanc.tools.myandroidtools.utils.ProcessInfoHelper.ProcessInfo

class ProcessListAdapter(
    private val processList: List<ProcessInfo>
) : Filterable, RecyclerView.Adapter<ProcessListAdapter.ViewHolder>() {

    private val memoryUnits = arrayOf("KB", "MB", "GB")

    private var filteredList: List<ProcessInfo> = listOf()

    init {
        filter.filter("")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIconHolder: ImageView
        val processIsolatedHolder: TextView
        val processNameHolder: TextView
        val processPidHolder: TextView
        val processUidHolder: TextView
        val processMemoryHolder: TextView
        lateinit var processInfoHolder: ProcessInfo

        init {
            ViewProcessListBinding.bind(view).apply {
                appIconHolder = appIcon
                processIsolatedHolder = processIsolated
                processNameHolder = processName
                processPidHolder = processPid
                processUidHolder = processUid
                processMemoryHolder = processMemory
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_process_list, parent, false)
            .let { return ViewHolder(it) }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val processInfo = filteredList[position]
        holder.apply {
            var memory = processInfo.memory
            var index = 0
            while (memory >= 1024) {
                memory /= 1024
                index++
            }

            appIconHolder.setImageDrawable(processInfo.icon)
            processNameHolder.text = processInfo.name
            processPidHolder.text = "PID: ${processInfo.pid}"
            processUidHolder.text = "UID: ${processInfo.uid}"
            processMemoryHolder.text = Html.fromHtml(
                "PSS Memory Usage: <b>$memory ${memoryUnits[index]}</b>", 0
            )

            if (processInfo.isolated) {
                processIsolatedHolder.visibility = View.VISIBLE
            } else {
                processIsolatedHolder.visibility = View.GONE
            }

            processInfoHolder = processInfo
        }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter() = object : Filter() {
        override fun performFiltering(filter: CharSequence): FilterResults {
            val results = mutableListOf<ProcessInfo>()
            val query = filter.toString().lowercase()
            for (info in processList) {
                if (query == "" || info.name.lowercase().contains(query) ||
                    info.pid.toString().contains(query) || info.uid.toString().contains(query)) {

                    results.add(info)
                }
            }
            return FilterResults().apply { values = results }
        }

        @Suppress("Unchecked_Cast")
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(filter: CharSequence, results: FilterResults) {
            filteredList = results.values as List<ProcessInfo>
            notifyDataSetChanged()
        }

    }
}