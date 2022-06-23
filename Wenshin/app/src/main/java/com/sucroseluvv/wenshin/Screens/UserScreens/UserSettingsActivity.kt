package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.requests.ChangeAccountInfoRequest
import com.sucroseluvv.wenshin.models.responses.UserAccountResponse
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import retrofit2.Response
import javax.inject.Inject

class UserSettingsActivity : AppCompatActivity() {

    var originalPhone: String? = null

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        App.Inst.AppComponent.inject(this)
        title="Настройки аккаунта"
        createActivity()
    }

    val consumer = object: SingleObserver<Response<UserAccountResponse>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<UserAccountResponse>) {
            val response = t.body()
            if(response != null) {
                originalPhone = response.phone
                numberTv.setText(originalPhone!!)
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
            ErrorDialogFragment(e.message ?: "Ошибка получения данных аккаунта").show(supportFragmentManager, "error")
        }
    }

    fun someHasChanged(): Boolean {
        if(originalPhone != null && !originalPhone.equals(numberTv.text.toString())) {
            return true
        }
        if(!TextUtils.isEmpty(newPass1.text.toString()) && !TextUtils.isEmpty(newPass2.text.toString())) {
            return true
        }

        return false
    }

    fun validateChanges(): ChangeAccountInfoRequest? {
        var newPassword: String? = null
        var newPhone: String? = null
        if(!TextUtils.isEmpty(newPass1.text.toString()) && !TextUtils.isEmpty(newPass2.text.toString())) {
            if(!newPass2.text.toString().equals(newPass1.text.toString())) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return null
            }
            if(newPass1.text.toString().length < 6) {
                Toast.makeText(this, "Длина пароля должна быть не менее 6 символов", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        if(originalPhone != null && !originalPhone.equals(numberTv.text.toString())) {
            newPhone = numberTv.text.toString()
        }

        return if(newPassword != null || newPhone != null) ChangeAccountInfoRequest(newPassword, newPhone) else null
    }

    fun showButton() {
        if(someHasChanged())
            save.visibility = View.VISIBLE
        else
            save.visibility = View.GONE
    }

    fun createActivity() {
        networkService.accountInfo().subscribe(consumer)

        newPass1.addTextChangedListener{ showButton() }
        newPass2.addTextChangedListener{ showButton() }
        numberTv.addTextChangedListener{ showButton() }

        save.setOnClickListener {
            val changes = validateChanges()
            if(changes != null)
                networkService.changeAccountInfo(changes).subscribe(consumer)
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}