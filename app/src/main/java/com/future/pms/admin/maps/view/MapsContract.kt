package com.future.pms.admin.maps.view

import com.google.android.gms.maps.GoogleMap

interface MapsContract {
  fun setMapLongClick(map: GoogleMap)
  fun setMapStyle(map: GoogleMap)
}