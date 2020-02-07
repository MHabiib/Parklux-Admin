package com.future.pms.admin

import com.future.pms.admin.barcode.network.BarcodeApi
import com.future.pms.admin.barcode.presenter.BarcodePresenter
import com.future.pms.admin.base.BaseTest
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class BarcodePresenterTest : BaseTest() {
  @Mock lateinit var barcodeApi: BarcodeApi
  @InjectMocks lateinit var barcodePresenter: BarcodePresenter

  @Test fun loadDataSuccess() {
    `when`(barcodeApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(Observable.just(parkingZone()))

    barcodePresenter.loadData(ACCESS_TOKEN)
  }

  @Test fun loadDataFailed() {
    `when`(barcodeApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    barcodePresenter.loadData(ACCESS_TOKEN)
  }

  @Test fun getQrImageSuccess() {
    `when`(barcodeApi.getQrImage(ACCESS_TOKEN, FCM_TOKEN)).thenReturn(
        Observable.just(ResponseBody.create(MediaType.parse(""), "")))

    barcodePresenter.getQrImage(FCM_TOKEN, ACCESS_TOKEN)
  }

  @Test fun getQrImageFailed() {
    `when`(barcodeApi.getQrImage(ACCESS_TOKEN, FCM_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    barcodePresenter.getQrImage(FCM_TOKEN, ACCESS_TOKEN)
  }

}