package com.future.pms.admin.ui.home

import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.SectionDetails

interface HomeContract {
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun getLayoutSuccess(slotsLayout: String)
  fun getSectionDetailsSuccess(listSectionDetails: List<SectionDetails>)
  fun updateParkingSectionSuccess(response: String)
  fun getLevelsSuccess(listLevel: List<ListLevel>)
  fun getLayoutFailed(error: String)
}