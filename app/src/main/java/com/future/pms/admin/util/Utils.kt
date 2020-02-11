package com.future.pms.admin.util

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import com.future.pms.admin.R
import com.future.pms.admin.util.Constants.Companion.FULL_DATE_TIME_FORMAT
import com.future.pms.admin.util.Constants.Companion.SHORT_MONTH_DATE_TIME_FORMAT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.DecimalFormat
import java.util.*

class Utils {
  companion object {

    fun convertLongToTime(time: Long): String {
      val date = Date(time)
      val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(FULL_DATE_TIME_FORMAT)
      } else {
        TODO("VERSION.SDK_INT < N")
      }
      return format.format(date)
    }

    fun convertLongToTimeShortMonth(time: Long): String {
      val date = Date(time)
      val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(SHORT_MONTH_DATE_TIME_FORMAT, Locale.ENGLISH)
      } else {
        TODO("VERSION.SDK_INT < N")
      }
      return format.format(date)
    }

    fun thousandSeparator(int: Int): String {
      return DecimalFormat("#,###,###").format(int)
    }

    fun simpleDialogMessage(context: Context, title: String, message: String) {
      context.let {
        MaterialAlertDialogBuilder(it).setTitle(title).setMessage(message).setPositiveButton(
            android.R.string.yes, null).setIcon(R.drawable.logo_blue).show()
      }
    }
  }
}