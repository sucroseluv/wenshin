package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sucroseluvv.wenshin.R

class InfoAboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_about)
        title="Инфомация о салоне"
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