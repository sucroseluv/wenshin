package com.sucroseluvv.wenshin.network.interceptors

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor(context: Context) : Interceptor {
    val context = context
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val response = chain.proceed(request)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

//        if(response.code == 500) {
//            Toast.makeText(context, "Ошибка: Ошибка со стороны сервера", Toast.LENGTH_SHORT).show()
//        }
//        if (response.code == 403) {
//            Toast.makeText(context, "Ошибка: Отсутствуют необходимые права доступа", Toast.LENGTH_SHORT).show()
//        }
//        if (response.code == 401) {
//            Toast.makeText(context, "Ошибка: Пользователь неавторизован", Toast.LENGTH_SHORT).show()
//        }
//
//
//        Toast.makeText(context, "response " + response.code, Toast.LENGTH_SHORT).show()
        return response
    }
}