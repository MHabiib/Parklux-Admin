package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.profile.network.ProfileApi
import com.future.pms.admin.profile.presenter.ProfilePresenter
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ProfilePresenterTest : BaseTest() {
  @Mock lateinit var profileApi: ProfileApi
  @InjectMocks lateinit var profilePresenter: ProfilePresenter
  private val picture: MultipartBody.Part = Mockito.mock(MultipartBody.Part::class.java)

  @Test fun loadDataSuccess() {
    `when`(profileApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(Observable.just(parkingZone()))

    profilePresenter.loadData(ACCESS_TOKEN)
  }

  @Test fun loadDataFailed() {
    `when`(profileApi.getParkingZoneDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.loadData(ACCESS_TOKEN)
  }

  @Test fun updateSuccess() {
    `when`(profileApi.updateParkingZone(ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.just(parkingZone()))

    profilePresenter.update(ACCESS_TOKEN, parkingZone())
  }

  @Test fun updateFailed() {
    `when`(profileApi.updateParkingZone(ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.update(ACCESS_TOKEN, parkingZone())
  }

  @Test fun addPictureSuccess() {
    `when`(profileApi.updateParkingZonePicture(ACCESS_TOKEN, picture)).thenReturn(
        Observable.just(STR))

    profilePresenter.addPicture(ACCESS_TOKEN, picture)
  }

  @Test fun addPictureFailed() {
    `when`(profileApi.updateParkingZonePicture(ACCESS_TOKEN, picture)).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.addPicture(ACCESS_TOKEN, picture)
  }
}