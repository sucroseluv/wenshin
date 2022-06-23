package com.sucroseluvv.wenshin.models.responses

import java.util.*

data class ScheduleOrderResponse(
    val orderId: Int,
    val sessionId: Int,
    val date: Date,
    val hours: Array<Int>,
    val username: String,
    val image: String,
    val sketchname: String,
    val masterId: Int?
)
