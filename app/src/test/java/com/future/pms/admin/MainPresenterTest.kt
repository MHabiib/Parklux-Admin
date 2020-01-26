package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.main.presenter.MainPresenter
import com.future.pms.admin.main.view.MainContract
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class MainPresenterTest : BaseTest() {
  @Mock lateinit var mainContract: MainContract
  @InjectMocks lateinit var mainPresenter: MainPresenter

  @Test fun attach() {
    mainPresenter.attach(mainContract)
  }

  @Test fun onHomeIconClick() {
    mainPresenter.onHomeIconClick()
  }

  @Test fun onBarcodeIconClick() {
    mainPresenter.onBarcodeIconClick()
  }

  @Test fun onActivityListIconClick() {
    mainPresenter.onActivityListIconClick()
  }

  @Test fun onProfileIconClick() {
    mainPresenter.onProfileIconClick()
  }

  @Test fun showEditLevel() {
    mainPresenter.showEditLevel(ID, LEVEL_NAME, STR)
  }

  @Test fun onBackPressedUpdateLevel() {
    mainPresenter.onBackPressedUpdateLevel()
  }

}