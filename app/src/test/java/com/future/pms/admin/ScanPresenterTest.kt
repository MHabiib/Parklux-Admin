package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.scan.network.ScanApi
import com.future.pms.admin.scan.presenter.ScanPresenter
import com.future.pms.admin.scan.view.ScanContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ScanPresenterTest : BaseTest() {
  @Mock lateinit var scanApi: ScanApi
  @Mock lateinit var scanContract: ScanContract
  @InjectMocks lateinit var scanPresenter: ScanPresenter

  @Test fun checkoutBookingStepTwoSuccess() {
    `when`(scanApi.checkoutStepTwo(ID, FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.just(customerBooking()))

    scanPresenter.checkoutBookingStepTwo(ID, FCM_TOKEN, ACCESS_TOKEN)

    verify(scanContract).bookingSuccess(customerBooking())
  }

  @Test fun checkoutBookingStepTwoFailed() {
    `when`(scanApi.checkoutStepTwo(ID, FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    scanPresenter.checkoutBookingStepTwo(ID, FCM_TOKEN, ACCESS_TOKEN)

    verify(scanContract).onFailed(ERROR)
  }
}