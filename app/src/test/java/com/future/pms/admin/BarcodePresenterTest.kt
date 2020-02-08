package com.future.pms.admin

import com.future.pms.admin.barcode.network.BarcodeApi
import com.future.pms.admin.barcode.presenter.BarcodePresenter
import com.future.pms.admin.barcode.view.BarcodeContract
import com.future.pms.admin.base.BaseTest
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class BarcodePresenterTest : BaseTest() {
  @Mock lateinit var barcodeApi: BarcodeApi
  @Mock lateinit var barcodeContract: BarcodeContract
  @InjectMocks lateinit var barcodePresenter: BarcodePresenter

  @Test fun loadDataSuccess() {
    `when`(barcodeApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(Observable.just(parkingZone()))

    barcodePresenter.loadData(ACCESS_TOKEN)

    verify(barcodeContract).showProgressTop(true)
    verify(barcodeContract).showProgressTop(false)
    verify(barcodeContract).loadCustomerDetailSuccess(parkingZone())
  }

  @Test fun loadDataFailed() {
    `when`(barcodeApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    barcodePresenter.loadData(ACCESS_TOKEN)

    verify(barcodeContract).showProgressTop(true)
    verify(barcodeContract).showProgressTop(false)
    verify(barcodeContract).onFailed(ERROR)
  }

  @Test fun getQrImageSuccess() {
    val responseBody = ResponseBody.create(MediaType.parse(""), "")
    `when`(barcodeApi.getQrImage(ACCESS_TOKEN, FCM_TOKEN)).thenReturn(Observable.just(responseBody))

    barcodePresenter.getQrImage(FCM_TOKEN, ACCESS_TOKEN)

    verify(barcodeContract).showProgress(true)
    verify(barcodeContract).showProgress(false)
  }

  @Test fun getQrImageFailed() {
    `when`(barcodeApi.getQrImage(ACCESS_TOKEN, FCM_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    barcodePresenter.getQrImage(FCM_TOKEN, ACCESS_TOKEN)

    verify(barcodeContract).showProgress(true)
    verify(barcodeContract).showProgress(false)
    verify(barcodeContract).onFailed(ERROR)
  }
}