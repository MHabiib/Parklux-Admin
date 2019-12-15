package com.future.pms.admin.util

class SpinnerItem(val idItem: String, val itemString: String, val isHint: Boolean) {
  override fun toString(): String {
    return this.itemString            // What to display in the Spinner list.
  }
}