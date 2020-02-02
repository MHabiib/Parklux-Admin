package com.future.pms.admin.activitylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.admin.R
import com.future.pms.admin.core.model.response.ongoingpastbooking.Content
import com.future.pms.admin.util.Utils
import java.util.*

class PaginationPastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var bookingList: MutableList<Content>? = LinkedList()
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  override fun onCreateViewHolder(parent: ViewGroup,
      viewType: Int): RecyclerView.ViewHolder = BookingViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    val booking = bookingList?.get(position)
    if (getItemViewType(position) == item) {
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
  }

  override fun getItemCount(): Int = bookingList?.size ?: 0

  override fun getItemViewType(position: Int): Int = if (position == bookingList?.size?.minus(
          1) && isLoadingAdded) loading else item

  fun add(booking: Content) {
    bookingList?.add(booking)
    bookingList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<Content>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() = bookingList?.clear()

  inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val customerName: TextView = itemView.findViewById<View>(R.id.tv_customer_name) as TextView
    val customerPhone: TextView = itemView.findViewById<View>(R.id.tv_customer_phone) as TextView
    val slot: TextView = itemView.findViewById<View>(R.id.tv_slot) as TextView
    val totalPrice: TextView = itemView.findViewById<View>(R.id.tv_total_price) as TextView
    val timeRang: TextView = itemView.findViewById<View>(R.id.tv_time_range) as TextView
  }
}
