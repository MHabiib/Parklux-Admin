package com.future.pms.admin.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.future.pms.admin.R

class CustomAdapter(context: Context, resource: Int, objects: List<SpinnerItem>) :
  ArrayAdapter<SpinnerItem>(context, resource, objects) {

  @SuppressLint("ResourceAsColor") override fun getDropDownView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    val listItem = super.getDropDownView(position, convertView, parent) as TextView
    if (position == 0) {
      listItem.setTextColor(R.color.darkGrey)
      listItem.gravity = Gravity.CENTER
    }
    return listItem
  }

  override fun isEnabled(position: Int): Boolean {
    return position != 0
  }
}