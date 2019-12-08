package com.future.pms.admin.di.module

import com.future.pms.admin.network.ApiServiceInterface
import com.future.pms.admin.network.RetrofitClient
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {
    @Provides
    fun provideApiService(): ApiServiceInterface {
        return RetrofitClient.create()
    }
}