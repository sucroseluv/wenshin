package com.sucroseluvv.wenshin.Screens.GuestScreens

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.requests.RegisterRequest
import com.sucroseluvv.wenshin.models.responses.SuccessResponse
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Response
import javax.inject.Inject


class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        App.Inst.AppComponent.inject(this)
        title="Регистрация"
        createActivity()
    }
    fun checkValidation(): Boolean {
        if(TextUtils.isEmpty(email.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            Toast.makeText(this, "Неверный формат почты", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!password.text.toString().equals(repeatPassword.text.toString())) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.text.toString().length < 6) {
            Toast.makeText(this, "Длина пароля должна быть не менее 6 символов", Toast.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(firstname.text.toString())) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(lastname.text.toString())) {
            Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(middlename.text.toString())) {
            Toast.makeText(this, "Введите отчество", Toast.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(phone.text.toString())) {
            Toast.makeText(this, "Введите телефон", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    val consumer = object: SingleObserver<Response<SuccessResponse>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribe")
        }

        override fun onSuccess(t: Response<SuccessResponse>) {
            val success = t.body()
            if(success != null && success.success) {
                Toast.makeText(this@RegisterActivity, "Успешная регистрация", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
            ErrorDialogFragment(e.message ?: "Ошибка регистрации").show(supportFragmentManager, "error")
        }

    }

    fun createActivity() {
        register.setOnClickListener {
            if(checkValidation()) {
                networkService.register(RegisterRequest(
                    email.text.toString(),
                    password.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString(),
                    middlename.text.toString(),
                    phone.text.toString()
                )).subscribe(consumer)
            }
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