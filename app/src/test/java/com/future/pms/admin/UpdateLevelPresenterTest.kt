package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.updatelevel.network.UpdateLevelApi
import com.future.pms.admin.updatelevel.presenter.UpdateLevelPresenter
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class UpdateLevelPresenterTest : BaseTest() {
  @Mock lateinit var updateLevelApi: UpdateLevelApi
  @InjectMocks lateinit var updateLevelPresenter: UpdateLevelPresenter

  @Test fun updateParkingLevelSuccess() {
    `when`(updateLevelApi.updateParkingLevel(ACCESS_TOKEN, levelDetailsRequest())).thenReturn(
        Observable.just(STR))

    updateLevelPresenter.updateParkingLevel(ACCESS_TOKEN, levelDetailsRequest())
  }

  @Test fun updateParkingLevelFailed() {
    `when`(updateLevelApi.updateParkingLevel(ACCESS_TOKEN, levelDetailsRequest())).thenReturn(
        Observable.error(Exception(ERROR)))

    updateLevelPresenter.updateParkingLevel(ACCESS_TOKEN, levelDetailsRequest())
  }
}