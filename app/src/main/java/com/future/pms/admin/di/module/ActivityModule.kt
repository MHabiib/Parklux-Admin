package com.future.pms.admin.di.module

import android.app.Activity
import com.future.pms.admin.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: Activity) {
    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): MainPresenter {
        return MainPresenter()
    }

}