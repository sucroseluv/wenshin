package com.sucroseluvv.wenshin.Screens.GuestScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sucroseluvv.wenshin.R
import kotlinx.android.synthetic.main.activity_main_user.*

class MainGuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_guest)

        val navView: BottomNavigationView = user_bottom_nav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.user_nav_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(setOf(
            R.id.mainGuestFragment,
            R.id.accountGuestFragment
        ))
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
    }
}