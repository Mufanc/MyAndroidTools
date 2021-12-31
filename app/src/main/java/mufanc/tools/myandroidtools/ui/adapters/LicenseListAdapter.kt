package mufanc.tools.myandroidtools.ui.adapters

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ViewLicenseListBinding

class LicenseListAdapter(
    private val context: Context,
    private val licenseList: Array<String>
) : RecyclerView.Adapter<LicenseListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var projectNameHolder: TextView
        var licenseNameHolder: TextView
        var followLinkHolder: ImageView

        init {
            ViewLicenseListBinding.bind(view).apply {
                projectNameHolder = projectName
                licenseNameHolder = license
                followLinkHolder = followLink
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_license_list, parent, false)
            .let { return ViewHolder(it) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val licenseInfo = licenseList[position].split("|")
        holder.apply {
            projectNameHolder.text = licenseInfo[0].trim()
            licenseNameHolder.text = licenseInfo[1].trim()
            followLinkHolder.setOnClickListener {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(licenseInfo[2].trim())
                }.let { context.startActivity(it) }
            }
        }
    }

    override fun getItemCount() = licenseList.size
}