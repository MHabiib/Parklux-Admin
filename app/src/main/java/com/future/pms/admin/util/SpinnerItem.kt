package com.future.pms.admin.util

class SpinnerItem(val idItem: String, val itemString: String, val itemStatus: String) {
  override fun toString(): String {
    return this.itemString            // What to display in the Spinner list.
  }
}