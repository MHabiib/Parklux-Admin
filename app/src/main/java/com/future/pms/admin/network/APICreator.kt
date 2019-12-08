package com.future.pms.admin.network

import com.future.pms.admin.network.NetworkConstant.BASE
import com.future.pms.admin.network.NetworkConstant.PASSWORD
import com.future.pms.admin.network.NetworkConstant.READ_TIMEOUT
import com.future.pms.admin.network.NetworkConstant.USERNAME
import com.future.pms.admin.network.NetworkConstant.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APICreator<out API>(
    private val clazz: Class<API>, private var headers: HashMap<String, String> = HashMap()
) {
    private fun getOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(
            BasicAuthInterceptor(USERNAME, PASSWORD)
        ).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
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
        val retrofit = Retrofit.Builder().baseUrl(BASE).client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(clazz)
    }
}