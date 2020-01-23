package com.future.pms.admin.activitylist.injection

import com.future.pms.admin.activitylist.network.ActivityListApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ActivityListModule {
  @Provides fun provideActivityListApi(retrofit: Retrofit): ActivityListApi {
    return retrofit.create(ActivityListApi::class.java)
  }
}