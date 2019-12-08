package com.future.pms.admin.util

class Constants {
    companion object {
        const val AUTHENTCATION = "authentication"
        const val HOME_FRAGMENT = "HomeFragment"
        const val SCAN_FRAGMENT = "ScanFragment"
        const val PARKING_DETAIL_FRAGMENT = "ParkingDetailFragment"
        const val PARKING_LAYOUT_FRAGMENT = "ParkingLayoutFragment"
        const val PROFILE_FRAGMENT = "ProfileFragment"
        const val BARCODE_FRAGMENT = "BarcodeFragment"

        const val RECEIPT_FRAGMENT = "Receipt Fragment"
        const val ONGOING_FRAGMENT = "Ongoin Fragment"
        const val BOOKING_DETAIL_FRAGMENT = "Booking Detail Fragment"
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refreshToken"
        const val ERROR = "Error"
        const val NULL = "null"
        const val ID_BOOKING = "idBooking"

        const val FULL_DATE_TIME_FORMAT = "HH:mm - dd MMMM yyyy "
        const val TIME_FORMAT = "HH:mm"

        const val parkSize = 100
        const val parkMargin = 8
        const val parkPadding = 4 * 8
        const val STATUS_AVAILABLE = 1
        const val STATUS_BOOKED = 2
        const val STATUS_RESERVED = 3
        const val STATUS_ROAD = 4
        const val ADD_NEW_LINE = '/'
        const val TAKEN_SLOT = 'U'
        const val EMPTY_SLOT = 'A'
        const val DISABLED_SLOT = 'R'
        const val SPACING = '_'
        const val SLOT = "Slot "
        var selectedIds = ""
    }
}