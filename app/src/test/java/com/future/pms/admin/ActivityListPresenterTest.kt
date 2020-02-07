package com.future.pms.admin

import com.future.pms.admin.activitylist.network.ActivityListApi
import com.future.pms.admin.activitylist.presenter.ActivityListPresenter
import com.future.pms.admin.base.BaseTest
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ActivityListPresenterTest : BaseTest() {
  @Mock lateinit var activityListApi: ActivityListApi
  @InjectMocks lateinit var activityListPresenter: ActivityListPresenter

  @Test fun findPastBookingParkingZoneSuccess() {
    `when`(activityListApi.findPastBookingParkingZone(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.just(booking()))

    activityListPresenter.findPastBookingParkingZone(ACCESS_TOKEN, PAGE)
  }

  @Test fun findPastBookingParkingZoneFailed() {
    `when`(activityListApi.findPastBookingParkingZone(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.error(Exception(ERROR)))

    activityListPresenter.findPastBookingParkingZone(ACCESS_TOKEN, PAGE)
  }

  @Test fun findOngoingBookingParkingZoneSuccess() {
    `when`(activityListApi.findOngoingBookingParkingZone(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.just(booking()))

    activityListPresenter.findOngoingBookingParkingZone(ACCESS_TOKEN, PAGE)
  }

  @Test fun findOngoingBookingParkingZoneFailed() {
    `when`(activityListApi.findOngoingBookingParkingZone(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.error(Exception(ERROR)))

    activityListPresenter.findOngoingBookingParkingZone(ACCESS_TOKEN, PAGE)
  }
}