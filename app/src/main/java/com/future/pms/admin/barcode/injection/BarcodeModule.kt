package com.future.pms.admin.barcode.injection

import com.future.pms.admin.barcode.network.BarcodeApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class BarcodeModule {
  @Provides fun provideBarcodeApi(retrofit: Retrofit): BarcodeApi {
    return retrofit.create(BarcodeApi::class.java)
  }
}