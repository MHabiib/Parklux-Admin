package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.home.network.HomeApi
import com.future.pms.admin.home.presenter.HomePresenter
import com.future.pms.admin.home.view.HomeContract
import com.future.pms.admin.util.Constants.Companion.EXIT_EDIT_MODE
import io.reactivex.Observable
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class HomePresenterTest : BaseTest() {
  @Mock lateinit var homeApi: HomeApi
  @Mock lateinit var homeContract: HomeContract
  @InjectMocks lateinit var homePresenter: HomePresenter

  @Test fun getParkingLayoutSuccess() {
    `when`(homeApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.getParkingLayout(ID, ACCESS_TOKEN)

    verify(homeContract).getLayoutSuccess(STR)
  }

  @Test fun getParkingLayoutFailed() {
    `when`(homeApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.getParkingLayout(ID, ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

  @Test fun getLevelsSuccess() {
    `when`(homeApi.getLevels(ACCESS_TOKEN)).thenReturn(Observable.just(ArgumentMatchers.anyList()))

    homePresenter.getLevels(ACCESS_TOKEN)

    verify(homeContract).showProgress(true)
    verify(homeContract).showProgress(false)
    verify(homeContract).getLevelsSuccess(ArgumentMatchers.anyList())
  }

  @Test fun getLevelsFailed() {
    `when`(homeApi.getLevels(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    homePresenter.getLevels(ACCESS_TOKEN)

    verify(homeContract).showProgress(true)
    verify(homeContract).showProgress(false)
    verify(homeContract).onFailed(ERROR)
  }

  @Test fun addParkingLevelSuccess() {
    `when`(homeApi.addParkingLevel(NAME, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.addParkingLevel(NAME, ACCESS_TOKEN)

    verify(homeContract).showProgressAddLevel(true)
    verify(homeContract).showProgressAddLevel(false)
    verify(homeContract).addParkingLevelSuccess(STR)
  }

  @Test fun addParkingLevelFailed() {
    `when`(homeApi.addParkingLevel(NAME, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.addParkingLevel(NAME, ACCESS_TOKEN)

    verify(homeContract).showProgressAddLevel(true)
    verify(homeContract).showProgressAddLevel(false)
    verify(homeContract).onFailed(ERROR)
  }

  @Test fun getSectionDetailsSuccess() {
    `when`(homeApi.getSectionDetails(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(listOf(sectionDetails())))

    homePresenter.getSectionDetails(ID, ACCESS_TOKEN)

    verify(homeContract).getSectionDetailsSuccess(listOf(sectionDetails()))
  }

  @Test fun getSectionDetailsFailed() {
    `when`(homeApi.getSectionDetails(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.getSectionDetails(ID, ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

  @Test fun updateParkingSectionSuccess() {
    `when`(homeApi.updateParkingSection(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.updateParkingSection(ID, ACCESS_TOKEN)

    verify(homeContract).updateParkingSectionSuccess(STR)
  }

  @Test fun updateParkingSectionFailed() {
    `when`(homeApi.updateParkingSection(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.updateParkingSection(ID, ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

  @Test fun updateLevelSuccess() {
    `when`(homeApi.updateLevel(ID, STR, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.updateLevel(ID, STR, ACCESS_TOKEN)

    verify(homeContract).updateParkingLayoutSuccess(STR)
  }

  @Test fun updateLevelFailed() {
    `when`(homeApi.updateLevel(ID, STR, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))
    `when`(homeApi.editModeParkingLevel(ID, EXIT_EDIT_MODE, ACCESS_TOKEN)).thenReturn(
        Observable.just(STR))

    homePresenter.updateLevel(ID, STR, ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

  @Test fun editModeParkingLevelSuccess() {
    `when`(homeApi.editModeParkingLevel(ID, STR, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.editModeParkingLevel(ID, STR, ACCESS_TOKEN)

    verify(homeContract).editModeParkingLevelSuccess(STR)
  }

  @Test fun editModeParkingLevelFailed() {
    `when`(homeApi.editModeParkingLevel(ID, STR, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.editModeParkingLevel(ID, STR, ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

}