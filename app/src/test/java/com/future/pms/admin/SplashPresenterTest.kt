package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.splash.network.SplashApi
import com.future.pms.admin.splash.presenter.SplashPresenter
import com.future.pms.admin.splash.view.SplashContract
import com.future.pms.admin.util.Constants.Companion.GRANT_TYPE_REFRESH
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class SplashPresenterTest : BaseTest() {
  @Mock lateinit var splashApi: SplashApi
  @Mock lateinit var splashContract: SplashContract
  @InjectMocks lateinit var splashPresenter: SplashPresenter

  @Test fun refreshTokenSuccess() {
    Mockito.`when`(splashApi.refresh(GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.just(token()))

    splashPresenter.refreshToken(REFRESH_TOKEN)

    Mockito.verify(splashContract).onSuccess(token())
  }

  @Test fun refreshTokenFailed() {
    Mockito.`when`(splashApi.refresh(GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    splashPresenter.refreshToken(REFRESH_TOKEN)

    Mockito.verify(splashContract).onLogin()
  }
}