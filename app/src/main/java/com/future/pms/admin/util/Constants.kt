package com.future.pms.admin.util

class Constants {
  companion object {
    const val AUTHENTCATION = "authentication"
    const val HOME_FRAGMENT = "HomeFragment"
    const val PROFILE_FRAGMENT = "ProfileFragment"
    const val BARCODE_FRAGMENT = "BarcodeFragment"

    const val SLOT_TAKEN = 'T'
    const val SLOT_EMPTY = 'E'
    const val SLOT_DISABLE = 'D'
    const val SLOT_ROAD = 'R'
    const val SLOT_NULL = '_'
    const val SLOT_READY = 'O'
    const val SLOT_SCAN_ME = 'S'
    const val LEVEL_AVAILABLE = 'A'

    const val SELECT_LEVEL = "Select level"
    val SECTION_ONE = "Section 1"
    val SECTION_TWO = "Section 2"
    val SECTION_THREE = "Section 3"
    val SECTION_FOUR = "Section 4"

    val NOT_ACTIVE = "NOT_ACTIVE"
    val ACTIVE = "ACTIVE"

    const val TOKEN = "token"
    const val ERROR = "Error"

    const val parkSize = 100
    const val parkMargin = 8
    const val parkPadding = 4 * 8
    const val STATUS_AVAILABLE = 1
    const val STATUS_BOOKED = 2
    const val STATUS_RESERVED = 3
    const val STATUS_ROAD = 4

    const val EDIT_MODE = 0
    const val EXIT_EDIT_MODE = 1
  }
}