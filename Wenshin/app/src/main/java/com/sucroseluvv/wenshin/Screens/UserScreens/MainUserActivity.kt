package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sucroseluvv.wenshin.R
import kotlinx.android.synthetic.main.activity_main_user.*

class MainUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        val navView: BottomNavigationView = user_bottom_nav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.user_nav_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(setOf(
            R.id.mainUserFragment,
            R.id.ordersUserFragment,
            R.id.accountUserFragment
        ))
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
    }
}