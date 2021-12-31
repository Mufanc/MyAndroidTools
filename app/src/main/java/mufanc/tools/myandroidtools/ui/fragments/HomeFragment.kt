package mufanc.tools.myandroidtools.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.FragmentHomeBinding
import mufanc.tools.myandroidtools.databinding.ViewFilterApplicationBinding
import mufanc.tools.myandroidtools.ui.adapters.ApplicationListAdapter
import mufanc.tools.myandroidtools.utils.AppInfoHelper
import mufanc.tools.myandroidtools.utils.FilterHelper
import mufanc.tools.myandroidtools.utils.SortBy
import kotlin.concurrent.thread

class HomeFragment : Fragment() {
    private var adapter: ApplicationListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        with (binding) {
            applist.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            ).let { applist.addItemDecoration(it) }

            fun listener(reload: Boolean = false) {
                refresh.isRefreshing = true
                thread {
                    AppInfoHelper.getAppInfoList(reload).let {
                        requireActivity().runOnUiThread {
                            adapter = ApplicationListAdapter(it)
                            applist.adapter = adapter
                            refresh.isRefreshing = false
                        }
                    }
                }
            }
            refresh.setOnRefreshListener { listener(true) }
            refresh.post { listener() }
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_home_menu, menu)

        menu.findItem(R.id.search_app).actionView
            .let { it as SearchView }
            .setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(query: String): Boolean {
                        FilterHelper.queryString = query
                        adapter?.filter?.filter("")
                        return true
                    }

                    override fun onQueryTextSubmit(p0: String?) = false
                }
            )

        menu.findItem(R.id.filter_app).also {
            it.setOnMenuItemClickListener {
                val binding = ViewFilterApplicationBinding.inflate(layoutInflater)
                PopupWindow(context).apply {
                    contentView = binding.root
                    isOutsideTouchable = true
                    isFocusable = true
                    elevation = contentView.findViewById<CardView>(R.id.popup_card).elevation
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    with (binding) {
                        when (FilterHelper.sortBy) {
                            SortBy.APP_NAME -> sortByAppname.isChecked = true
                            SortBy.UPDATE_TIME -> sortByUpdateTime.isChecked = true
                            SortBy.UID -> sortByUid.isChecked = true
                        }
                        sortReverse.isChecked = FilterHelper.reverse
                        hideSystemApps.isChecked = FilterHelper.hideSystemApps
                    }

                    contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    val icon = activity?.findViewById<View>(R.id.filter_app)!!
                    showAsDropDown(
                        icon,
                        -contentView.measuredWidth+icon.width-20, 0
                    )
                }.setOnDismissListener {
                    with (binding) {
                        FilterHelper.sortBy = when {
                            sortByAppname.isChecked -> SortBy.APP_NAME
                            sortByUpdateTime.isChecked -> SortBy.UPDATE_TIME
                            sortByUid.isChecked -> SortBy.UID
                            else -> throw IllegalStateException("Unexpected Key!")
                        }
                        FilterHelper.reverse = sortReverse.isChecked
                        FilterHelper.hideSystemApps = hideSystemApps.isChecked
                    }
                    adapter?.filter?.filter("")
                }
                true
            }
        }
    }
}