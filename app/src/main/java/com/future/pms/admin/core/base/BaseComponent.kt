package com.future.pms.admin.core.base

import android.content.Context
import com.future.pms.admin.BaseApp
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Component(modules = [BaseModule::class, BaseNetworkModule::class]) interface BaseComponent {
  fun inject(baseApp: BaseApp)

  fun context(): Context

  fun gson(): Gson

  fun genericRetrofit(): Retrofit

  fun okHttpClient(): OkHttpClient
}