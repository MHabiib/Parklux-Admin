package com.future.pms.admin.login.injection

import com.future.pms.admin.login.network.LoginApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class LoginModule {
  @Provides fun provideLoginApi(retrofit: Retrofit): LoginApi {
    return retrofit.create(LoginApi::class.java)
  }
}