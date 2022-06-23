package com.sucroseluvv.wenshin.network.interceptors

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(context: Context) : Interceptor {
    val context = context
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request
            .newBuilder()
            .addHeader("appid", "wenshin")
            .addHeader("deviceplatform", "android")
            .removeHeader("User-Agent")
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = preferences.getString("userToken", "")
        if(token != null && token != "") {
            requestBuilder.addHeader("Authorization", token)
        }

        request = requestBuilder.build()
        val response = chain.proceed(request)
        return response
    }
}