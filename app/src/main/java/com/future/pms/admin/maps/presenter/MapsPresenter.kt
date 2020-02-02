package com.future.pms.admin.maps.presenter

import com.future.pms.admin.maps.view.MapsContract
import com.google.android.gms.maps.GoogleMap
import javax.inject.Inject

class MapsPresenter @Inject constructor() {
  private lateinit var view: MapsContract

  fun attach(view: MapsContract) {
    this.view = view
  }

  fun setMapLongClick(map: GoogleMap) {
    view.setMapLongClick(map)
  }

  fun setMapStyle(map: GoogleMap) {
    view.setMapStyle(map)
  }
}