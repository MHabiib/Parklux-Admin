package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.login.network.LoginApi
import com.future.pms.admin.login.presenter.LoginPresenter
import com.future.pms.admin.login.view.LoginContract
import com.future.pms.admin.util.Constants.Companion.GRANT_TYPE
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class LoginPresenterTest : BaseTest() {
  @Mock lateinit var loginApi: LoginApi
  @Mock lateinit var loginContract: LoginContract
  @InjectMocks lateinit var loginPresenter: LoginPresenter

  @Test fun loginSuccess() {
    `when`(loginApi.auth(USERNAME, PASSWORD, GRANT_TYPE)).thenReturn(Observable.just(token()))

    loginPresenter.login(USERNAME, PASSWORD)
  }

  @Test fun loginFailed() {
    `when`(loginApi.auth(USERNAME, PASSWORD, GRANT_TYPE)).thenReturn(
        Observable.error(Exception(ERROR)))

    loginPresenter.login(USERNAME, PASSWORD)
  }

  @Test fun loadDataSuccess() {
    `when`(loginApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(Observable.just(parkingZone()))

    loginPresenter.loadData(ACCESS_TOKEN)
  }

  @Test fun loadDataFailed() {
    `when`(loginApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    loginPresenter.loadData(ACCESS_TOKEN)
  }

}