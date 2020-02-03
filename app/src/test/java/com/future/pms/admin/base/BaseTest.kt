package com.future.pms.admin.base

import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.request.LevelDetailsRequest
import com.future.pms.admin.core.model.response.ParkingZoneResponse
import com.future.pms.admin.core.model.response.SectionDetails
import com.future.pms.admin.core.model.response.ongoingpastbooking.Booking
import com.future.pms.admin.core.model.response.ongoingpastbooking.Pageable
import com.future.pms.admin.core.model.response.ongoingpastbooking.Sort
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

open class BaseTest {
  @get:Rule val rxSchedulerRule = RxSchedulerRule()

  @Before fun onSetup() {
    MockitoAnnotations.initMocks(this)
  }

  protected val ACCESS_TOKEN = "accessToken"
  protected val REFRESH_TOKEN = "refreshToken"
  protected val FCM_TOKEN = "fcmToken"
  protected val SUCCESS = "success"
  protected val ERROR = "error"
  protected val ID = "id"
  protected val PARKING_LAYOUT = "parkingLayout"
  protected val USERNAME = "username"
  protected val EMAIL = "email"
  protected val PASSWORD = "password"
  protected val NAME = "name"
  protected val PHONE_NUMBER = "phoneNumber"
  protected val ROLE = "role"
  protected val LEVEL_NAME = "levelName"
  protected val LAYOUT = "layout"
  protected val PAGE = 0
  protected val FILTER = "filter"
  protected val STR = "empty"

  protected fun booking(): Booking {
    return Booking(emptyList(), empty = false, first = false, last = false, number = 0,
        numberOfElements = 0, pageable = page(), size = 0, sort = sort(), totalElements = 0,
        totalPages = 0)
  }

  protected fun parkingZone(): ParkingZoneResponse {
    return ParkingZoneResponse("address", "emailAdmin", "name", "openHour", "password",
        "phoneNumber", 0.0, "imageUrl", 0.0, 0.0)
  }

  protected fun levelDetailsRequest(): LevelDetailsRequest {
    return LevelDetailsRequest("idLevel", "levelName", "status")
  }

  protected fun sectionDetails(): SectionDetails {
    return SectionDetails("idSection", "sectionName", "status", 0, 0, 0)
  }

  protected fun token(): Token {
    return Token("scope", "token_type", 0L, "refresh_token", "id_token", "access_token")
  }

  private fun sort(): Sort {
    return Sort(empty = false, sorted = false, unsorted = false)
  }

  private fun page(): Pageable {
    return Pageable(0, 0, 0, false, sort(), false)
  }
}