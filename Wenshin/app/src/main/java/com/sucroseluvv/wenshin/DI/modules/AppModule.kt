package com.sucroseluvv.wenshin.DI.modules

import android.content.Context
import com.sucroseluvv.wenshin.network.NetworkService
import com.sucroseluvv.wenshin.network.ServiceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(context: Context) {
    val context = context

    @Provides
    @Singleton
    fun provideContext() : Context {
        return context
    }
}