package com.sucroseluvv.wenshin.models.requests

import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderSeansesUserActivity
import com.sucroseluvv.wenshin.models.Session

data class SessionRequest (val date: String, val hours: Array<Int>)
fun getSessionRequestFromSession(session: Session): SessionRequest {
    val day = session.date.date
    val month = session.date.month
    val formatDay = if (day < 10) "0" + day else day
    val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
    val date = "${session.date.year}-${formatMonth}-${formatDay}"
    return SessionRequest(date, session.hours)
}
fun getSessionsRequestList(session: Array<Session>): Array<SessionRequest> {
    return session.map { s -> getSessionRequestFromSession(s) }.toTypedArray()
}

data class CreateOrderRequest (val sketchId: Int, val masterId: Int, val sessions: Array<SessionRequest>)