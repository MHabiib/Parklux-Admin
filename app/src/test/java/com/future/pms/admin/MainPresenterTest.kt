package com.future.pms.admin

import com.future.pms.admin.base.BaseTest
import com.future.pms.admin.main.presenter.MainPresenter
import com.future.pms.admin.main.view.MainContract
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class MainPresenterTest : BaseTest() {
  @Mock lateinit var mainContract: MainContract
  @InjectMocks lateinit var mainPresenter: MainPresenter

  @Test fun attach() {
    mainPresenter.attach(mainContract)
  }

  @Test fun onHomeIconClick() {
    mainPresenter.onHomeIconClick()
    Mockito.verify(mainContract).showHomeFragment()
  }

  @Test fun onBarcodeIconClick() {
    mainPresenter.onBarcodeIconClick()
    Mockito.verify(mainContract).showBarcodeFragment()
  }

  @Test fun onActivityListIconClick() {
    mainPresenter.onActivityListIconClick()
    Mockito.verify(mainContract).showActivityListFragment()
  }

  @Test fun onProfileIconClick() {
    mainPresenter.onProfileIconClick()
    Mockito.verify(mainContract).showProfileFragment()
  }

  @Test fun showEditLevel() {
    mainPresenter.showEditLevel(ID, LEVEL_NAME, STR, 1)
    Mockito.verify(mainContract).showEditLevel(ID, LEVEL_NAME, STR, 1)
  }

  @Test fun onBackPressedUpdateLevel() {
    mainPresenter.onBackPressedUpdateLevel()
    Mockito.verify(mainContract).onBackPressedUpdateLevel()
  }

}