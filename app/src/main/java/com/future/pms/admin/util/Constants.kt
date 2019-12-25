package com.future.pms.admin.util

class Constants {
  companion object {
    const val AUTHENTCATION = "authentication"
    const val HOME_FRAGMENT = "HomeFragment"
    const val PROFILE_FRAGMENT = "ProfileFragment"
      const val ACTIVITY_LIST_FRAGMENT = "ActivityListFragment"
    const val BARCODE_FRAGMENT = "BarcodeFragment"

      const val TIME_FORMAT = "HH:mm"
    const val SHORT_MONTH_DATE_TIME_FORMAT = "HH:mm - dd MMM yy"
    const val FULL_DATE_TIME_FORMAT = "HH:mm - dd MMMM yy"

    const val SLOT_TAKEN = 'T'
    const val SLOT_EMPTY = 'E'
    const val SLOT_DISABLE = 'D'
    const val SLOT_ROAD = 'R'
    const val SLOT_NULL = '_'
    const val SLOT_READY = 'O'
    const val SLOT_SCAN_ME = 'S'
    const val LEVEL_AVAILABLE = 'A'

    const val SELECT_LEVEL = "Select level"
    const val SECTION_ONE = "Section 1"
    const val SECTION_TWO = "Section 2"
    const val SECTION_THREE = "Section 3"
    const val SECTION_FOUR = "Section 4"

    const val NOT_ACTIVE = "NOT_ACTIVE"
    const val ACTIVE = "ACTIVE"

    const val TOKEN = "token"
    const val ERROR = "Error"

      const val parkSize = 88
      const val parkMargin = 0
      const val parkPadding = 0
    const val STATUS_AVAILABLE = 1
    const val STATUS_BOOKED = 2
    const val STATUS_RESERVED = 3
    const val STATUS_ROAD = 4

    const val EDIT_MODE = 0
    const val EXIT_EDIT_MODE = 1
  }
}