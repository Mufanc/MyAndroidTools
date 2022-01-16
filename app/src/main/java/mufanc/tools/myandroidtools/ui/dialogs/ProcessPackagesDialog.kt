package mufanc.tools.myandroidtools.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.DialogProcessPackageListBinding
import mufanc.tools.myandroidtools.databinding.ViewAppListBinding
import mufanc.tools.myandroidtools.ui.AppDetailActivity
import mufanc.tools.myandroidtools.utils.AppInfoHelper

class ProcessPackagesDialog(context: Context, pkgList: List<String>) {

    private class ApplicationListAdapter(
        private val appList: List<AppInfoHelper.AppInfo>
    ) : RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>() {

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


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_app_list, parent, false)
                .let { return ViewHolder(it) }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val appInfo = appList[position]
            with (holder) {
                appIconHolder.setImageDrawable(appInfo.iconCopy())
                appUidHolder.text = "(${appInfo.uid})"
                appNameHolder.text = appInfo.appName
                packageNameHolder.text = appInfo.packageName

                itemView.setOnClickListener {
                    Intent(itemView.context, AppDetailActivity::class.java).let {
                        it.putExtra("packageName", appInfo.packageName)
                        itemView.context.startActivity(it)
                    }
                }
            }
        }

        override fun getItemCount() = appList.size
    }

    private val dialog = AlertDialog.Builder(context)
        .setTitle(R.string.process_packages)
        .create()

    init {
        DialogProcessPackageListBinding
            .inflate(LayoutInflater.from(context))
            .apply {
                dialog.setView(this.root)
                applist.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                applist.adapter = ApplicationListAdapter(
                    pkgList.map { AppInfoHelper.getAppInfo(it) }
                )
            }
    }

    fun show() {
        dialog.show()
    }
}