package com.future.pms.admin.splash.injection

import com.future.pms.admin.splash.network.SplashApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class SplashModule {
  @Provides fun provideSplashApi(retrofit: Retrofit): SplashApi {
    return retrofit.create(SplashApi::class.java)
  }
}