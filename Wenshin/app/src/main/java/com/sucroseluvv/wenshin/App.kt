package com.sucroseluvv.wenshin

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.sucroseluvv.wenshin.DI.components.AppComponent
import com.sucroseluvv.wenshin.DI.components.DaggerAppComponent
import com.sucroseluvv.wenshin.DI.modules.AppModule
import com.sucroseluvv.wenshin.DI.modules.NetworkModule
import com.sucroseluvv.wenshin.DI.modules.UserModule
import com.sucroseluvv.wenshin.common.WenshinNotificationManager
import java.util.*

class App : Application {
    companion object {
        private lateinit var inst: App
        val Inst: App get() = inst
    }
    private lateinit var appComponent: AppComponent
    val AppComponent: AppComponent get() = appComponent

    constructor(){
        inst = this
    }

    override fun onCreate() {
        super.onCreate()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .userModule(UserModule(prefs))
            .networkModule(NetworkModule(this))
            .build()
    }
}