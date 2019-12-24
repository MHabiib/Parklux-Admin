package com.future.pms.admin.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(context: Context, resource: Int, objects: List<SpinnerItem>) :
  ArrayAdapter<SpinnerItem>(context, resource, objects) {

  @SuppressLint("ResourceAsColor") override fun getDropDownView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
      return super.getDropDownView(position, convertView, parent) as TextView
  }

  override fun isEnabled(position: Int): Boolean {
    return position != 0
  }
}