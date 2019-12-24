package com.future.pms.admin.ui.activitylist

import android.content.Context
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

class PaginationAdapter(private val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var bookingList: MutableList<Content>? = null
    private var isLoadingAdded = false

    init {
        bookingList = LinkedList()
    }

    fun setBookingList(bookingList: MutableList<Content>) {
        this.bookingList = bookingList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.item_list, parent, false)
                viewHolder = BookingViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val booking = bookingList?.get(position)
        when (getItemViewType(position)) {
            ITEM -> {
                val bookingViewHolder = holder as BookingViewHolder
                bookingViewHolder.customerName.text = booking?.customerName
                bookingViewHolder.customerPhone.text = booking?.customerPhone
                bookingViewHolder.slot.text = booking?.slotName
                bookingViewHolder.totalPrice.text = booking?.totalPrice
                bookingViewHolder.timeRang.text = String.format("%s > %s",
                    booking?.dateIn?.let { Utils.convertLongToTimeOnly(it) },
                    booking?.dateOut?.let { Utils.convertLongToTimeOnly(it) })
            }

            LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return bookingList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == bookingList?.size?.minus(1) && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Content())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = bookingList?.size?.minus(1)
        val result = position?.let { getItem(it) }

        if (result != null) {
            bookingList?.removeAt(position)
            notifyItemRemoved(position)
        }
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

    private fun getItem(position: Int): Content? {
        return bookingList?.get(position)
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val customerName: TextView = itemView.findViewById<View>(R.id.tv_customer_name) as TextView
        val customerPhone: TextView =
            itemView.findViewById<View>(R.id.tv_customer_phone) as TextView
        val slot: TextView = itemView.findViewById<View>(R.id.tv_slot) as TextView
        val totalPrice: TextView = itemView.findViewById<View>(R.id.tv_total_price) as TextView
        val timeRang: TextView = itemView.findViewById<View>(R.id.tv_time_range) as TextView

    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val progressBar: ProgressBar = itemView.findViewById<View>(
            R.id.loadmore_progress
        ) as ProgressBar

    }

    companion object {
        private val LOADING = 0
        private val ITEM = 1
    }


}
