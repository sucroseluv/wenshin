package com.sucroseluvv.wenshin.Screens.UserScreens.MenuFragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.Common.AuthUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.FavoritesUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.HistoryUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserSettingsActivity
import kotlinx.android.synthetic.main.fragment_account_user.*

class AccountUserFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_user, container, false)
        createAccount(view)
        return view
    }

    fun createAccount(view: View) {
        val favoritesButton = view.findViewById<ConstraintLayout>(R.id.favorites)
        val logoutButton = view.findViewById<ConstraintLayout>(R.id.logout)
        val historyButton = view.findViewById<ConstraintLayout>(R.id.history)
        val settingsButton = view.findViewById<ConstraintLayout>(R.id.settings)

        historyButton.setOnClickListener {
            val intent = Intent(activity,HistoryUserActivity::class.java)
            startActivity(intent)
        }

        favoritesButton.setOnClickListener {
            val intent = Intent(activity,FavoritesUserActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(activity,UserSettingsActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener{
            AlertDialog.Builder(activity)
                .setMessage("Вы уверенны что хотите выйти?")
                .setNegativeButton("Нет", { dialog, id -> })
                .setPositiveButton("Да") { dialog, id ->
                    val prefs =
                        PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
                    val editor = prefs.edit()
                    editor.remove("userRole")
                    editor.remove("userToken")
                    editor.commit()
                    val intent = Intent(activity, AuthUserActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .create().show()
        }
    }

}