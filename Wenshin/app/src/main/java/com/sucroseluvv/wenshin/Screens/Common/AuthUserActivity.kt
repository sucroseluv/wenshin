package com.sucroseluvv.wenshin.Screens.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.GuestScreens.MainGuestActivity
import com.sucroseluvv.wenshin.Screens.MasterScreens.MainMasterActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.MainUserActivity
import com.sucroseluvv.wenshin.models.UserInfo
import com.sucroseluvv.wenshin.models.UserType
import javax.inject.Inject

class AuthUserActivity : AppCompatActivity() {

    @Inject
    lateinit var userInfo: UserInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_user)
        App.Inst.AppComponent.inject(this)
        var intent: Intent
        when (userInfo.role) {
            UserType.user -> intent = Intent(this, MainUserActivity::class.java)
            UserType.master -> intent = Intent(this, MainMasterActivity::class.java)
            else -> intent = Intent(this, MainGuestActivity::class.java)
        }
        startActivity(intent)
        this.finish()
    }
}