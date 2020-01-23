package com.future.pms.admin.core.base

import com.future.pms.admin.core.network.BasicAuthInterceptor
import com.future.pms.admin.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module class BaseNetworkModule {
  @Provides fun provideGson(): Gson {
    return GsonBuilder().setLenient().create()
  }

  @Provides fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(
        BasicAuthInterceptor(Constants.USERNAME, Constants.PASSWORD)).writeTimeout(
        Constants.WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(Constants.READ_TIMEOUT,
        TimeUnit.SECONDS).build()
  }

  @Provides fun provideNetworkManager(gson: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create(gson)).addCallAdapterFactory(
        RxJava2CallAdapterFactory.create()).baseUrl(Constants.BASE).client(client).build()
  }
}