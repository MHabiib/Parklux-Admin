package com.future.pms.admin.ui.activitylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.admin.R
import com.future.pms.admin.model.response.ongoingpastbooking.Content
import com.future.pms.admin.util.Utils
import java.util.*

class PaginationAdapterPast : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var bookingList: MutableList<Content>? = null
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    bookingList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    var viewHolder: RecyclerView.ViewHolder? = null
    val inflater = LayoutInflater.from(parent.context)

    when (viewType) {
      item -> {
        val viewItem = inflater.inflate(R.layout.item_list, parent, false)
        viewHolder = BookingViewHolder(viewItem)
      }
      loading -> {
        val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
        viewHolder = LoadingViewHolder(viewLoading)
      }
    }
    return viewHolder!!
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    val booking = bookingList?.get(position)
    when (getItemViewType(position)) {
      item -> {
        val bookingViewHolder = holder as BookingViewHolder
        bookingViewHolder.customerName.text = booking?.customerName
        bookingViewHolder.customerPhone.text = booking?.customerPhone
        bookingViewHolder.slot.text = booking?.slotName
        bookingViewHolder.totalPrice.text = String.format("IDR %s",
            booking?.totalPrice?.toInt()?.let {
              Utils.thousandSeparator(it)
            })
        bookingViewHolder.timeRang.text = String.format("%s\n%s",
            booking?.dateIn?.let { Utils.convertLongToTimeShortMonth(it) },
            booking?.dateOut?.let { Utils.convertLongToTimeShortMonth(it) })
      }

      loading -> {
        val loadingViewHolder = holder as LoadingViewHolder
        loadingViewHolder.progressBar.visibility = View.VISIBLE
      }
    }
  }

  override fun getItemCount(): Int {
    return bookingList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == bookingList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(booking: Content) {
    bookingList?.add(booking)
    bookingList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<Content>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() {
    bookingList?.clear()
  }

  private fun getItem(position: Int): Content? {
    return bookingList?.get(position)
  }

  inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val customerName: TextView = itemView.findViewById<View>(R.id.tv_customer_name) as TextView
    val customerPhone: TextView = itemView.findViewById<View>(R.id.tv_customer_phone) as TextView
    val slot: TextView = itemView.findViewById<View>(R.id.tv_slot) as TextView
    val totalPrice: TextView = itemView.findViewById<View>(R.id.tv_total_price) as TextView
    val timeRang: TextView = itemView.findViewById<View>(R.id.tv_time_range) as TextView
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById<View>(
        R.id.loadmore_progress) as ProgressBar
  }
}
