package com.sucroseluvv.wenshin.network

import android.content.Context
import com.sucroseluvv.wenshin.network.interceptors.ErrorInterceptor
import com.sucroseluvv.wenshin.network.interceptors.HeaderInterceptor
import com.sucroseluvv.wenshin.network.interceptors.NetworkConnectionInterceptor
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceProvider(context: Context) {
    val context = context
    private var okhttp: OkHttpClient
    private var retrofit: Retrofit

    init {
        okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
            /*.addInterceptor(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                Log.w("Retrofit@Response", response.body!!.string())
                chain
            })*/
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(HeaderInterceptor(context))
            .addInterceptor(ErrorInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        retrofit = Retrofit.Builder()
            .baseUrl(API.root)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()
    }

    fun <S> createService(service: Class<S>) : S {
        return retrofit.create(service)
    }
}