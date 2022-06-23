package com.sucroseluvv.wenshin.network.interceptors

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.sucroseluvv.wenshin.Screens.Common.NoInternetActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor : Interceptor {
    var context : Context
    var cm : ConnectivityManager
    constructor(context: Context) {
        this.context = context
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.getActiveNetworkInfo()
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnected()
        if(!isConnected) {
            //Toast.makeText(context, "Проблема подключения к сети, проверьте соединение.", Toast.LENGTH_SHORT).show()
//            throw object : IOException() {
//                override val message: String?
//                    get() = "Проблема подключения к сети, проверьте соединение."
//            }
            Log.d("connectivityManager", "true ${isConnected}")
            context.startActivity(Intent(context, NoInternetActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK))
        } else {
            Log.d("connectivityManager", "false ${isConnected}")
            //Toast.makeText(context, "инет работает.", Toast.LENGTH_SHORT).show()
        }

        val response = chain.proceed(request)
        return response
    }
}