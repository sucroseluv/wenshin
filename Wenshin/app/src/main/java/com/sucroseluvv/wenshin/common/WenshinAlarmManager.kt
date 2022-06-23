package com.sucroseluvv.wenshin.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sucroseluvv.wenshin.Screens.Common.AuthUserActivity
import java.util.*

class WenshinNotificationManager(context: Context) {
    val context = context

    fun createAlarm(calendar: Calendar) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val clockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, getNotificationInfoPendingIntent())
        alarmManager.setAlarmClock(clockInfo, getNotificationActionPendingIntent())
    }

    private fun getNotificationInfoPendingIntent(): PendingIntent? {
        val intent = Intent(context, AuthUserActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getNotificationActionPendingIntent(): PendingIntent? {
        val intent = Intent(context, AuthUserActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}