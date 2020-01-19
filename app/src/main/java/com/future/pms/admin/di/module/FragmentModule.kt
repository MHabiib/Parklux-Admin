package com.future.pms.admin.di.module

import androidx.fragment.app.Fragment
import com.future.pms.admin.ui.activitylist.ActivityListPresenter
import com.future.pms.admin.ui.barcode.BarcodePresenter
import com.future.pms.admin.ui.home.HomePresenter
import com.future.pms.admin.ui.profile.ProfilePresenter
import com.future.pms.admin.ui.updatelevel.UpdateLevelPresenter
import dagger.Module
import dagger.Provides

@Module class FragmentModule(private var fragment: Fragment) {
  @Provides fun provideFragment(): Fragment {
    return fragment
  }

  @Provides fun provideActivityListPresenter(): ActivityListPresenter {
    return ActivityListPresenter()
  }

  @Provides fun provideBarcodePresenter(): BarcodePresenter {
    return BarcodePresenter()
  }

  @Provides fun provideHomePresenter(): HomePresenter {
    return HomePresenter()
  }

  @Provides fun provideProfilePresenter(): ProfilePresenter {
    return ProfilePresenter()
  }

  @Provides fun provideUpdateLevelPresenter(): UpdateLevelPresenter {
    return UpdateLevelPresenter()
  }
}