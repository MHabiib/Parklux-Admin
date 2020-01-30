package com.future.pms.admin.activitylist.view

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.activitylist.adapter.PaginationAdapterOngoing
import com.future.pms.admin.activitylist.adapter.PaginationAdapterPast
import com.future.pms.admin.activitylist.injection.ActivityListComponent
import com.future.pms.admin.activitylist.injection.DaggerActivityListComponent
import com.future.pms.admin.activitylist.presenter.ActivityListPresenter
import com.future.pms.admin.core.base.BaseFragment
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.response.ongoingpastbooking.Booking
import com.future.pms.admin.databinding.FragmentActivityListBinding
import com.future.pms.admin.util.Constants.Companion.ACTIVITY_LIST_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.NO_CONNECTION
import com.future.pms.admin.util.Constants.Companion.ONGOING
import com.future.pms.admin.util.Constants.Companion.PAST
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.PaginationScrollListener
import com.google.gson.Gson
import javax.inject.Inject

class ActivityListFragment : BaseFragment(), ActivityListContract {
  private var daggerBuild: ActivityListComponent = DaggerActivityListComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ActivityListPresenter
  @Inject
  lateinit var gson: Gson
  private lateinit var binding: FragmentActivityListBinding
  private lateinit var paginationAdapterPast: PaginationAdapterPast
  private lateinit var paginationAdapterOngoing: PaginationAdapterOngoing
  private lateinit var accessToken: String
  private var currentPagePast = 0
  private var currentPageOngoing = 0
  private var isLastPagePast = false
  private var isLastPageOngoing = false
  private var isLoading = false

  companion object {
    const val TAG: String = ACTIVITY_LIST_FRAGMENT
  }

  fun newInstance(): ActivityListFragment = ActivityListFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false)
    binding.shimmerOngoing.startShimmerAnimation()
    binding.shimmerPast.startShimmerAnimation()
    val linearLayoutManagerPast = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val linearLayoutManagerOngoing = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
        false)
    binding.refreshOngoing.setOnRefreshListener {
      binding.shimmerOngoing.visibility = View.VISIBLE
      binding.shimmerOngoing.startShimmerAnimation()
      binding.noOrder.visibility = View.GONE
      paginationAdapterOngoing.clear()
      paginationAdapterOngoing.notifyDataSetChanged()
      currentPageOngoing = 0
      isLastPageOngoing = false
      presenter.findOngoingBookingParkingZone(accessToken, currentPageOngoing)
      binding.refreshOngoing.isRefreshing = false
    }
    binding.refreshPast.setOnRefreshListener {
      binding.shimmerPast.visibility = View.VISIBLE
      binding.shimmerPast.startShimmerAnimation()
      binding.noPast.visibility = View.GONE
      paginationAdapterPast.clear()
      paginationAdapterPast.notifyDataSetChanged()
      currentPagePast = 0
      isLastPagePast = false
      presenter.findPastBookingParkingZone(accessToken, currentPagePast)
      binding.refreshPast.isRefreshing = false
    }
    paginationAdapterPast = PaginationAdapterPast()
    paginationAdapterOngoing = PaginationAdapterOngoing()

    binding.rvPast.layoutManager = linearLayoutManagerPast
    binding.rvPast.adapter = this.paginationAdapterPast
    binding.rvPast.addOnScrollListener(object :
        PaginationScrollListener(linearLayoutManagerPast, isLastPagePast) {
      override fun loadMoreItems() {
        if (!isLoading && !isLastPagePast) {
          isLoading = true
          loadPastNextPage()
        }
      }
    })
    binding.rvOngoing.layoutManager = linearLayoutManagerOngoing
    binding.rvOngoing.adapter = this.paginationAdapterOngoing
    binding.rvOngoing.addOnScrollListener(object :
        PaginationScrollListener(linearLayoutManagerOngoing, isLastPageOngoing) {
      override fun loadMoreItems() {
        if (!isLoading && !isLastPageOngoing) {
          isLoading = true
          loadOngoingNextPage()
        }
      }
    })
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    accessToken = gson.fromJson(
      context?.getSharedPreferences(AUTHENTICATION, MODE_PRIVATE)?.getString(TOKEN, null),
      Token::class.java
    ).accessToken
    presenter.findPastBookingParkingZone(accessToken, currentPagePast)
    presenter.findOngoingBookingParkingZone(accessToken, currentPagePast)
  }

  private fun loadOngoingNextPage() =
    presenter.findOngoingBookingParkingZone(accessToken, currentPagePast)

  private fun loadPastNextPage() =
    presenter.findPastBookingParkingZone(accessToken, currentPagePast)

  override fun findPastBookingParkingZoneSuccess(booking: Booking) {
    binding.shimmerPast.visibility = View.GONE
    binding.shimmerPast.stopShimmerAnimation()
    if (currentPagePast != 0) {
      if (currentPagePast <= booking.totalPages - 1) {
        paginationAdapterPast.addAll(booking.content)
        currentPagePast += 1
      } else {
        isLastPagePast = true
      }
    } else {
      if (booking.content.isEmpty()) {
        binding.noPast.visibility = View.VISIBLE
      }
      paginationAdapterPast.addAll(booking.content)
      if (currentPagePast >= booking.totalPages - 1) {
        isLastPagePast = true
      } else {
        currentPagePast += 1
      }
    }
    isLoading = false
  }

  override fun findOngoingBookingParkingZoneSuccess(booking: Booking) {
    binding.shimmerOngoing.visibility = View.GONE
    binding.shimmerOngoing.stopShimmerAnimation()
    if (currentPageOngoing != 0) {
      if (currentPageOngoing <= booking.totalPages - 1) {
        paginationAdapterOngoing.addAll(booking.content)
        currentPageOngoing += 1
      } else {
        isLastPageOngoing = true
      }
    } else {
      paginationAdapterOngoing.addAll(booking.content)
      if (booking.content.isEmpty()) {
        binding.noOrder.visibility = View.VISIBLE
      }
      if (currentPageOngoing >= booking.totalPages - 1) {
        isLastPageOngoing = true
      } else {
        currentPageOngoing += 1
      }
    }
    isLoading = false
  }

  override fun onFailed(message: String) {
    when (message) {
      PAST -> {
        binding.shimmerPast.visibility = View.GONE
        binding.shimmerPast.stopShimmerAnimation()
        isLastPagePast = true
      }
      ONGOING -> {
        binding.shimmerOngoing.visibility = View.GONE
        binding.shimmerOngoing.stopShimmerAnimation()
        isLastPageOngoing = true
      }
      NO_CONNECTION -> {
        Toast.makeText(context, getString(R.string.no_network_connection),
            Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}