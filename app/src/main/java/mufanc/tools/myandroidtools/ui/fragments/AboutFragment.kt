package mufanc.tools.myandroidtools.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mufanc.tools.myandroidtools.BuildConfig
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.FragmentAboutBinding
import mufanc.tools.myandroidtools.ui.adapters.LicenseListAdapter

class AboutFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentAboutBinding.inflate(inflater).apply {
            versionName.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            licenseList.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                addItemDecoration(DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                ))
                adapter = LicenseListAdapter(
                    context,
                    resources.getStringArray(R.array.license_list)
                )
            }
            return root
        }
    }
}