package mufanc.tools.myandroidtools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mufanc.tools.myandroidtools.databinding.FragmentProcessBinding
import mufanc.tools.myandroidtools.ui.adapters.ProcessListAdapter
import mufanc.tools.myandroidtools.utils.ProcessInfoHelper
import kotlin.concurrent.thread

class ProcessFragment : Fragment() {

    private lateinit var binding: FragmentProcessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProcessBinding.inflate(inflater)
        binding.apply {
            processList.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            ).let { processList.addItemDecoration(it) }

            fun listener() {
                refresh.isRefreshing = true
                thread {
                    ProcessInfoHelper.getRunningAppProcesses().let {
                        activity?.runOnUiThread {
                            processList.adapter = ProcessListAdapter(it)
                            refresh.isRefreshing = false
                        }
                    }
                }
            }
            refresh.setOnRefreshListener { listener() }
            refresh.post { listener() }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            processList.adapter = null
        }
    }
}