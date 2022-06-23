package com.sucroseluvv.wenshin.DI.modules

import android.content.Context
import android.content.SharedPreferences
import com.sucroseluvv.wenshin.models.UserInfo
import com.sucroseluvv.wenshin.models.UserType
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Module
class UserModule(sharedPreferences: SharedPreferences) {
    val preferences: SharedPreferences = sharedPreferences

    @Inject
    lateinit var context: Context
    @Provides
    fun provideSharedUserInfo(): UserInfo {
        val role = preferences.getString("userRole", "guest")
        val token = preferences.getString("userToken", "")
        var userInfo: UserInfo?
        if(role == "master")
            userInfo = UserInfo(UserType.master, token)
        else if (role == "user")
            userInfo = UserInfo(UserType.user, token)
        else
            userInfo = UserInfo(UserType.guest, null)
        return userInfo!!
    }

}