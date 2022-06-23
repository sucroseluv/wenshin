package com.sucroseluvv.wenshin.models

import java.util.*

data class Session(val id: Int?, val date: Date, val hours : Array<Int>, val status: String?, val order_id: Int?, val isPaid: Int? = null)

fun getSessionHoursString (session: Session): String {
    val count = session.hours.size % 10
    var hourText = "час"
    if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
    if(arrayOf(5,6,7,8,9).any { n -> n==count }) hourText = "часов"
    return "${session.hours.size} ${hourText}"
}