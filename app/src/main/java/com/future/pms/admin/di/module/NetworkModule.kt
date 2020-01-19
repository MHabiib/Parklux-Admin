package com.future.pms.admin.di.module

import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.BasicAuthInterceptor
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.BASE
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module class NetworkModule {

  @Singleton @Provides fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl(BASE).addCallAdapterFactory(
        RxJava2CallAdapterFactory.create()).addConverterFactory(
        GsonConverterFactory.create(GsonBuilder().setLenient().create())).build()
  }
}

class APICreator<out API>(private val clazz: Class<API>,
    private var headers: HashMap<String, String> = HashMap()) {

  companion object Factory {
    fun create(): ApiServiceInterface {
      val retrofit = retrofit2.Retrofit.Builder().baseUrl(BASE).addCallAdapterFactory(
          RxJava2CallAdapterFactory.create()).addConverterFactory(
          GsonConverterFactory.create(GsonBuilder().setLenient().create())).build()

      return retrofit.create(ApiServiceInterface::class.java)
    }
  }

  private fun getOkHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder().addInterceptor(
        BasicAuthInterceptor(Constants.USERNAME, Constants.PASSWORD)).writeTimeout(
        Constants.WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(Constants.READ_TIMEOUT,
        TimeUnit.SECONDS)
  }

  fun generate(): API {
    val okHttpClient = getOkHttpBuilder()
    okHttpClient.addNetworkInterceptor { chain ->
      val original = chain.request()
      val requestBuilder = original.newBuilder()
      headers.map {
        requestBuilder.addHeader(it.key, it.value)
      }
      requestBuilder.method(original.method(), original.body())
      val request = requestBuilder.build()
      chain.proceed(request)
    }
    val client = okHttpClient.build()
    val retrofit = Retrofit.Builder().baseUrl(BASE).client(client).addCallAdapterFactory(
        RxJava2CallAdapterFactory.create()).addConverterFactory(
        GsonConverterFactory.create()).build()
    return retrofit.create(clazz)
  }
}