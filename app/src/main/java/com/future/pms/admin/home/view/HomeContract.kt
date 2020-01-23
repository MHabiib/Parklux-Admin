package com.future.pms.admin.home.view

import com.future.pms.admin.core.base.BaseView
import com.future.pms.admin.core.model.response.ListLevel
import com.future.pms.admin.core.model.response.SectionDetails

interface HomeContract : BaseView {
  fun getLayoutSuccess(slotsLayout: String)
  fun getSectionDetailsSuccess(listSectionDetails: List<SectionDetails>)
  fun updateParkingSectionSuccess(response: String)
  fun updateParkingLayoutSuccess(response: String)
  fun getLevelsSuccess(listLevel: List<ListLevel>)
  fun addParkingLevelSuccess(response: String)
  fun editModeParkingLevelSuccess(response: String)
  fun showProgress(show: Boolean)
}