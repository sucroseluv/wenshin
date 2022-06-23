package com.sucroseluvv.wenshin.Screens.MasterScreens.MenuFragments

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
import com.sucroseluvv.wenshin.Screens.MasterScreens.UploadSketchActivity

class AccountMasterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_master, container, false)
        createAccount(view)
        return view
    }

    fun createAccount(view: View) {
        val logoutButton = view.findViewById<ConstraintLayout>(R.id.logout)
        val newSketch = view.findViewById<ConstraintLayout>(R.id.newSketch)

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
                .create()
                .show()
        }
        newSketch.setOnClickListener {
            startActivity(Intent(activity,UploadSketchActivity::class.java))
        }
    }
}