package mufanc.tools.myandroidtools.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.FragmentComponentListBinding
import mufanc.tools.myandroidtools.ui.AppDetailActivity
import mufanc.tools.myandroidtools.ui.adapters.ComponentListAdapter
import kotlin.concurrent.thread

class ComponentListFragment(
    private val packageName: String,
    private val tab: AppDetailActivity.ComponentTab,
    private val index: Int
) : Fragment() {
    lateinit var adapter: ComponentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val binding = FragmentComponentListBinding.inflate(inflater)

        with (binding) {
            componentList.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            ).let { componentList.addItemDecoration(it) }

            thread {
                val list = tab.getComponentList(packageName)
                requireActivity().runOnUiThread {
                    if (list.isEmpty()) noComponents.visibility = View.VISIBLE
                    ComponentListAdapter(packageName, list).let  {
                        componentList.adapter = it
                        adapter = it
                    }
                }
            }

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity()
            .findViewById<ViewPager2>(R.id.view_pager)
            .currentItem.let { if (it != index) return }

        inflater.inflate(R.menu.fragment_component_list_menu, menu)

        menu.findItem(R.id.search_component).actionView
            .let { it as SearchView }
            .setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(query: String): Boolean {
                        adapter.filter.filter(query)
                        return true
                    }

                    override fun onQueryTextSubmit(p0: String?) = false
                }
            )
    }
}