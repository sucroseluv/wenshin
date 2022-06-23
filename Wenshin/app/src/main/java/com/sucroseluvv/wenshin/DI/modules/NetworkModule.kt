package com.sucroseluvv.wenshin.DI.modules

import android.content.Context
import com.sucroseluvv.wenshin.network.NetworkService
import com.sucroseluvv.wenshin.network.ServiceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule(context: Context) {
    val context = context
    var networkService: NetworkService

    init {
        var serviceProvider: ServiceProvider = ServiceProvider(context)
        networkService = NetworkService(serviceProvider)
    }

    @Provides
    @Singleton
    fun provideNetwork() : NetworkService {
        return networkService
    }
}