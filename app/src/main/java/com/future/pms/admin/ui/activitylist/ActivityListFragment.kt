package com.future.pms.admin.ui.activitylist

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentActivityListBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.response.ongoingpastbooking.Booking
import com.future.pms.admin.util.Constants.Companion.ACTIVITY_LIST_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.AUTHENTCATION
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.PaginationScrollListener
import com.google.gson.Gson
import javax.inject.Inject

class ActivityListFragment : Fragment(), ActivityListContract {
    @Inject
    lateinit var presenter: ActivityListPresenter
    private lateinit var binding: FragmentActivityListBinding
    private lateinit var paginationAdapter: PaginationAdapter
    private lateinit var accessToken: String
    private var currentPage = 0
    private var isLastPage = false

    companion object {
        const val TAG: String = ACTIVITY_LIST_FRAGMENT
    }

    fun newInstance(): ActivityListFragment {
        return ActivityListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        accessToken = Gson().fromJson(
            context?.getSharedPreferences(AUTHENTCATION, MODE_PRIVATE)?.getString(
                TOKEN, null
            ), Token::class.java
        ).accessToken
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        paginationAdapter = PaginationAdapter(context)
        binding.rvPast.layoutManager = linearLayoutManager
        binding.rvPast.adapter = this.paginationAdapter
        binding.rvPast.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager, isLastPage) {
            override fun loadMoreItems() {
                if (!isLastPage) {
                    currentPage += 1
                    loadNextPage()
                }
            }
        })
        loadFirstPage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    private fun loadNextPage() {
        presenter.findPastBookingParkingZone(accessToken, currentPage)
    }

    private fun loadFirstPage() {
        presenter.findPastBookingParkingZone(accessToken, currentPage)
    }

    override fun findPastBookingParkingZoneSuccess(booking: Booking) {
        if (currentPage != 0) {
            paginationAdapter.removeLoadingFooter()
            paginationAdapter.addAll(booking.content)
            if (currentPage <= booking.totalPages) paginationAdapter.addLoadingFooter()
            else isLastPage = true
        } else {
            paginationAdapter.addAll(booking.content)
            if (currentPage <= booking.totalPages) paginationAdapter.addLoadingFooter()
            else isLastPage = true
        }
    }

    override fun findPastBookingParkingZoneFailed(response: String) {
        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
    }


    private fun injectDependency() {
        val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
            FragmentModule()
        ).build()
        profileComponent.inject(this)
    }
}