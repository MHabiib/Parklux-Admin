package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.maps.presenter.MapsPresenter
import com.future.pms.admin.maps.view.MapsContract
import com.google.android.gms.maps.GoogleMap
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class MapsPresenterTest : BaseTest() {
  @Mock lateinit var mapsContract: MapsContract
  @Mock lateinit var map: GoogleMap
  @InjectMocks lateinit var mapsPresenter: MapsPresenter

  @Test fun attach() {
    mapsPresenter.attach(mapsContract)
  }

  @Test fun setMapLongClick() {
    mapsPresenter.setMapLongClick(map)
  }

  @Test fun setMapStyle() {
    mapsPresenter.setMapStyle(map)
  }
}