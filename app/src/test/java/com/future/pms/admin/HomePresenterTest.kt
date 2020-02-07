package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.home.network.HomeApi
import com.future.pms.admin.home.presenter.HomePresenter
import io.reactivex.Observable
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class HomePresenterTest : BaseTest() {
  @Mock lateinit var homeApi: HomeApi
  @InjectMocks lateinit var homePresenter: HomePresenter

  @Test fun getParkingLayoutSuccess() {
    `when`(homeApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.getParkingLayout(ID, ACCESS_TOKEN)
  }

  @Test fun getParkingLayoutFailed() {
    `when`(homeApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.getParkingLayout(ID, ACCESS_TOKEN)
  }

  @Test fun getLevelsSuccess() {
    `when`(homeApi.getLevels(ACCESS_TOKEN)).thenReturn(Observable.just(ArgumentMatchers.anyList()))

    homePresenter.getLevels(ACCESS_TOKEN)
  }

  @Test fun getLevelsFailed() {
    `when`(homeApi.getLevels(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    homePresenter.getLevels(ACCESS_TOKEN)
  }

  @Test fun addParkingLevelSuccess() {
    `when`(homeApi.addParkingLevel(NAME, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.addParkingLevel(NAME, ACCESS_TOKEN)
  }

  @Test fun addParkingLevelFailed() {
    `when`(homeApi.addParkingLevel(NAME, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.addParkingLevel(NAME, ACCESS_TOKEN)
  }

  @Test fun getSectionDetailsSuccess() {
    `when`(homeApi.getSectionDetails(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(listOf(sectionDetails())))

    homePresenter.getSectionDetails(ID, ACCESS_TOKEN)
  }

  @Test fun getSectionDetailsFailed() {
    `when`(homeApi.getSectionDetails(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.getSectionDetails(ID, ACCESS_TOKEN)
  }

  @Test fun updateParkingSectionSuccess() {
    `when`(homeApi.updateParkingSection(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.updateParkingSection(ID, ACCESS_TOKEN)
  }

  @Test fun updateParkingSectionFailed() {
    `when`(homeApi.updateParkingSection(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.updateParkingSection(ID, ACCESS_TOKEN)
  }

  @Test fun updateLevelSuccess() {
    `when`(homeApi.updateLevel(ID, STR, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.updateLevel(ID, STR, ACCESS_TOKEN)
  }

  @Test fun updateLevelFailed() {
    `when`(homeApi.updateLevel(ID, STR, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.updateLevel(ID, STR, ACCESS_TOKEN)
  }

  @Test fun editModeParkingLevelSuccess() {
    `when`(homeApi.editModeParkingLevel(ID, STR, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    homePresenter.editModeParkingLevel(ID, STR, ACCESS_TOKEN)
  }

  @Test fun editModeParkingLevelFailed() {
    `when`(homeApi.editModeParkingLevel(ID, STR, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homePresenter.editModeParkingLevel(ID, STR, ACCESS_TOKEN)
  }

}