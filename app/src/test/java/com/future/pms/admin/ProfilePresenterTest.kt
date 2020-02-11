package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.profile.network.ProfileApi
import com.future.pms.admin.profile.presenter.ProfilePresenter
import com.future.pms.admin.profile.view.ProfileContract
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ProfilePresenterTest : BaseTest() {
  @Mock lateinit var profileApi: ProfileApi
  @Mock lateinit var profileContract: ProfileContract
  @InjectMocks lateinit var profilePresenter: ProfilePresenter
  private val picture: MultipartBody.Part = Mockito.mock(MultipartBody.Part::class.java)

  @Test fun loadDataSuccess() {
    `when`(profileApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(Observable.just(parkingZone()))

    profilePresenter.loadData(ACCESS_TOKEN)

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).loadParkingZoneDetailSuccess(parkingZone())
  }

  @Test fun loadDataFailed() {
    `when`(profileApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.loadData(ACCESS_TOKEN)

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).onFailed("java.lang.Exception: error")
  }

  @Test fun updateSuccess() {
    `when`(profileApi.updateParkingZone(ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.just(parkingZone()))

    profilePresenter.update(ACCESS_TOKEN, parkingZone())

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).onSuccess()
  }

  @Test fun updateFailed() {
    `when`(profileApi.updateParkingZone(ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.update(ACCESS_TOKEN, parkingZone())

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).onFailed(ERROR)
  }

  @Test fun addPictureSuccess() {
    `when`(profileApi.updateParkingZonePicture(ACCESS_TOKEN, picture)).thenReturn(
        Observable.just(STR))

    profilePresenter.addPicture(ACCESS_TOKEN, picture)

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).onSuccess()
  }

  @Test fun addPictureFailed() {
    `when`(profileApi.updateParkingZonePicture(ACCESS_TOKEN, picture)).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.addPicture(ACCESS_TOKEN, picture)

    verify(profileContract).showProgress(true)
    verify(profileContract).showProgress(false)
    verify(profileContract).onFailed(ERROR)
  }
}