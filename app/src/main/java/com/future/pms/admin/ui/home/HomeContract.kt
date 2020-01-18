package com.future.pms.admin.ui.home

import com.future.pms.admin.di.base.BaseView
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.SectionDetails

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