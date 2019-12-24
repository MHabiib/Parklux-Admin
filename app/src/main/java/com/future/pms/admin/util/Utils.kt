package com.future.pms.admin.util

import android.icu.text.SimpleDateFormat
import android.os.Build
import com.future.pms.admin.util.Constants.Companion.TIME_FORMAT
import java.util.*

class Utils {
    companion object {
        fun convertLongToTimeOnly(time: Long): String {
            val date = Date(time)
            val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat(TIME_FORMAT)
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            return format.format(date)
        }
    }
}