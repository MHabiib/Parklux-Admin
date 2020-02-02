package com.future.pms.admin.updatelevel.injection

import com.future.pms.admin.updatelevel.network.UpdateLevelApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class UpdateLevelModule {
  @Provides fun provideUpdateLevelApi(retrofit: Retrofit): UpdateLevelApi {
    return retrofit.create(UpdateLevelApi::class.java)
  }
}