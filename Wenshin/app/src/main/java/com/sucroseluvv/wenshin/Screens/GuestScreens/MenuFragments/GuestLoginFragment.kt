package com.sucroseluvv.wenshin.Screens.GuestScreens.MenuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.Common.AuthUserActivity
import com.sucroseluvv.wenshin.Screens.GuestScreens.RegisterActivity
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.requests.LoginRequest
import com.sucroseluvv.wenshin.models.responses.AuthResponse
import com.sucroseluvv.wenshin.models.responses.ErrorResponse
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class GuestLoginFragment : Fragment() {

    @Inject
    lateinit var networkService: NetworkService
    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.Inst.AppComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var res: View = inflater.inflate(R.layout.fragment_login_guest, container, false)
        createView(res)
        return res
    }

    val consumer = object: SingleObserver<Response<ResponseBody>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("network", "login consumer subscribed")
        }

        override fun onSuccess(res: Response<ResponseBody>) {
//            val mapped = Gson().fromJson(res.body()?.string(), Map::class.java)
            Log.d("network", "login consumer result ${res.code()}")
            if(res.code() >= 400) {
                ErrorDialogFragment(
                    Gson().fromJson(
                        res.errorBody()?.charStream()?.readText(),
                        ErrorResponse::class.java
                    ).error as String, "Ошибка авторизации"
                )
                    .show(activity!!.supportFragmentManager, "")
            }
            else {
                val authResponse = Gson().fromJson(
                    res.body()?.charStream()?.readText(),
                    AuthResponse::class.java
                )
                val prefs = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
                val editor = prefs.edit()
                editor.putString("userRole", authResponse.role)
                editor.putString("userToken", authResponse.token)
                editor.commit()
                val intent = Intent(activity, AuthUserActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }

        override fun onError(e: Throwable) {
            Log.d("network", "login consumer error: ${e.message}")
            if(e.message != null)
                ErrorDialogFragment(e.message!!).show(activity!!.supportFragmentManager, "")
        }

    }

    fun createView(view: View) {
        var email = view.findViewById<EditText>(R.id.loginField)
        var password = view.findViewById<EditText>(R.id.passwordField)
        var auth = view.findViewById<Button>(R.id.loginButton)

        auth.setOnClickListener {
            val request: LoginRequest = LoginRequest(email = email.text.toString(), password = password.text.toString())
            networkService.login(request).subscribe(consumer)
        }

        view.findViewById<TextView>(R.id.goto_register).setOnClickListener {
            startActivity(Intent(activity, RegisterActivity::class.java))
        }
    }
}