package com.sucroseluvv.wenshin.models.responses

import com.sucroseluvv.wenshin.models.Session

data class MasterOrderInfo(
    val id: Int,
    val status: String,
    val sketch_id: Int,
    val client_id: Int,
    val master_id: Int,
    val image: String,
    val sketchName: String,
    val description: String,
    val width: Float,
    val height: Float,
    val working_hours: Int,
    val name: String,
    val email: String,
    val phone: String,
    val avatar: String,
    val price: Float,
    val amount: Float,
    val sessions: Array<Session>
)